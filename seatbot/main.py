import logging
import requests
import os
from datetime import datetime, timedelta
from collections import Counter
from telegram import Update, ReplyKeyboardMarkup, ReplyKeyboardRemove, MenuButtonCommands, BotCommand
from telegram.ext import ApplicationBuilder, CommandHandler, ConversationHandler, MessageHandler, filters, ContextTypes

TG_BASE_URL = os.getenv("TG_BASE_URL")
TG_USERNAME = os.getenv("TG_USERNAME")
TG_PASSWORD = os.getenv("TG_PASSWORD")

TG_BOT_TOKEN = os.getenv("TG_BOT_TOKEN")

CHOOSING_EVENT = 1

logging.basicConfig(
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    level=logging.INFO
)

def login(base_url: str, username: str, password: str) -> str:
    url = f"{base_url}/auth/login"
    payload = {"username": username, "password": password}
 
    response = requests.post(url, json=payload)
    response.raise_for_status()
 
    data = response.json()
 
    token = data.get("token") or data.get("access_token") or data.get("jwt")
    if not token:
        raise ValueError(f"Could not find token in login response: {data}")
 
    print("Login successful.")
    return token

def get_events(base_url: str, token: str) -> list:
    url = f"{base_url}/events"
    headers = {"Authorization": f"Bearer {token}"}
 
    response = requests.get(url, headers=headers)
    response.raise_for_status()
 
    return response.json()

def get_free_seats(base_url: str, token: str, event_id: int) -> list:
    reserved_seats = 0
    free_seats = 0

    url = f"{base_url}/events/{event_id}/locations"
    headers = {"Authorization": f"Bearer {token}"}
 
    locations = requests.get(url, headers=headers)
    locations.raise_for_status()

    for location in locations.json():

        url = f"{base_url}/events/{event_id}/locations/{location['id']}/seats"
        headers = {"Authorization": f"Bearer {token}"}

        seats = requests.get(url, headers=headers)
        seats.raise_for_status()

        c = Counter(seat['reserved'] for seat in seats.json())

        reserved_seats += c[True]
        free_seats += c[False]

    return reserved_seats, free_seats + reserved_seats

async def get_token(app) -> str:
    now = datetime.now()
    expiry = app.bot_data.get("token_expiry")
    
    if expiry is None or now >= expiry:
        print("Token expired or missing, re-authenticating...")
        
        token = login(TG_BASE_URL, TG_USERNAME, TG_PASSWORD)

        app.bot_data["token"] = token
        app.bot_data["token_expiry"] = now + timedelta(hours=23, minutes=50)
        print("Token refreshed.")

    return app.bot_data["token"]

# Handlers
async def start(update: Update, context: ContextTypes.DEFAULT_TYPE):
    await update.message.reply_text("Bot is running! Send me any message.")

def make_events_handler():
    async def events(update: Update, context: ContextTypes.DEFAULT_TYPE):
        api_token = await get_token(context.application)
        events_list = get_events(TG_BASE_URL, api_token)

        context.user_data["events"] = {e["name"]: e for e in events_list}

        keyboard = [[e["name"]] for e in events_list]
        keyboard.append(["❌ Cancel"])

        await update.message.reply_text("Select event for more info:", reply_markup=ReplyKeyboardMarkup(keyboard, one_time_keyboard=True))

        return CHOOSING_EVENT

    async def event_chosen(update: Update, context: ContextTypes.DEFAULT_TYPE):
        chosen_name = update.message.text
        events = context.user_data.get("events", {})

        if chosen_name not in events:
            await update.message.reply_text("Please pick one from the list.")
            return CHOOSING_EVENT

        event = events[chosen_name]
        api_token = await get_token(context.application)
        reserved_seats, total_seats = get_free_seats(TG_BASE_URL, api_token, event['id'])

        await update.message.reply_text(
            f"*{event['name']}*\n"
            f"📆 Date: {event['date']}\n"
            f"📝 {event['description']}\n"
            f"🪑 Reserved: {reserved_seats} / {total_seats}",
            parse_mode="Markdown",
            reply_markup=ReplyKeyboardRemove()
        )
        return ConversationHandler.END

    async def cancel(update: Update, context: ContextTypes.DEFAULT_TYPE):
        context.user_data.clear()
        await update.message.reply_text("Cancelled.", reply_markup=ReplyKeyboardRemove())
        return ConversationHandler.END

    return ConversationHandler(
        entry_points=[CommandHandler("events", events)],
        states={
            CHOOSING_EVENT: [MessageHandler(filters.TEXT & ~filters.COMMAND, event_chosen)]
        },
        fallbacks=[
            CommandHandler("cancel", cancel),
            CommandHandler("events", events),
            MessageHandler(filters.Regex("❌ Cancel"), cancel)
            ],
    )

async def post_init(app):
    await get_token(app)
    await app.bot.set_my_commands([
        BotCommand("events", "Show upcoming events"),
    ])

    await app.bot.set_chat_menu_button(menu_button=MenuButtonCommands())


if __name__ == "__main__":

    app = ApplicationBuilder().token(TG_BOT_TOKEN).post_init(post_init).build()

    app.add_handler(CommandHandler("start", start))
    app.add_handler(make_events_handler())

    print("Bot is running. Press Ctrl+C to stop.")
    app.run_polling()