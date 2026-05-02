const TOKEN_KEY = "seatApiToken";
const BASE_URL_KEY = "seatApiBaseUrl";

function defaultApiBaseUrl() {
  // Prefer same-origin API under /api when served via http(s).
  // If opened from file:// (origin "null"), fall back to a relative /api.
  const origin = window.location.origin;
  if (origin && origin !== "null" && /^https?:/.test(window.location.protocol)) {
    return `${origin}/api`;
  }
  return "/api";
}

const state = {
  token: localStorage.getItem(TOKEN_KEY) || "",
  baseUrl: localStorage.getItem(BASE_URL_KEY) || defaultApiBaseUrl(),
  events: [],
  locations: [],
  seats: [],
  selectedSeatId: null
};

const getEl = (id) => document.getElementById(id);
const currentPage = window.location.pathname.split("/").pop() || "index.html";
const protectedPages = new Set(["seats.html", "manage.html"]);

function isProtectedPage() {
  return protectedPages.has(currentPage);
}

function redirectToLogin() {
  const next = `${currentPage}${window.location.search || ""}`;
  window.location.href = `./login.html?next=${encodeURIComponent(next)}`;
}

function readNextPageFromQuery() {
  const params = new URLSearchParams(window.location.search);
  const next = params.get("next");
  if (!next) {
    return "./seats.html";
  }
  // Keep redirects local to this frontend.
  if (next.includes("://") || next.startsWith("//")) {
    return "./seats.html";
  }
  return next.startsWith("./") ? next : `./${next}`;
}

function setOutput(message, data) {
  const output = getEl("output");
  if (!output) {
    return;
  }
  output.textContent = data ? `${message}\n${JSON.stringify(data, null, 2)}` : message;
}

function setAuthStatus() {
  const authStatus = getEl("authStatus");
  if (authStatus) {
    authStatus.textContent = state.token ? "Logged in" : "Not logged in";
  }
  updateAuthStateClass();
  updateAuthActionButton();
  updateAuthLinks();
}

function getBaseUrl() {
  const input = getEl("apiBaseUrl");
  const rawValue = input ? input.value.trim() : state.baseUrl;
  const normalized = (rawValue || defaultApiBaseUrl()).replace(/\/$/, "");
  state.baseUrl = normalized;
  localStorage.setItem(BASE_URL_KEY, normalized);
  return normalized;
}

function initializeBaseUrlInput() {
  const input = getEl("apiBaseUrl");
  if (!input) {
    return;
  }
  input.value = state.baseUrl;
  input.addEventListener("change", () => {
    getBaseUrl();
    setOutput("API base URL updated.", { baseUrl: state.baseUrl });
  });
}

async function apiRequest(path, options = {}) {
  const headers = { "Content-Type": "application/json", ...(options.headers || {}) };
  if (state.token) {
    headers.Authorization = `Bearer ${state.token}`;
  }

  const response = await fetch(`${getBaseUrl()}${path}`, { ...options, headers });
  const contentType = response.headers.get("content-type") || "";
  const body = contentType.includes("application/json") ? await response.json() : await response.text();

  if (!response.ok) {
    if (response.status === 401) {
      state.token = "";
      localStorage.removeItem(TOKEN_KEY);
      setAuthStatus();
      if (isProtectedPage()) {
        setOutput("Session expired. Redirecting to login.");
        setTimeout(redirectToLogin, 500);
      }
    }
    throw new Error(typeof body === "string" ? body : JSON.stringify(body));
  }
  return body;
}

function selectedEventId() {
  return getEl("eventSelect")?.value;
}

function selectedLocationId() {
  return getEl("locationSelect")?.value;
}

function renderEvents() {
  const select = getEl("eventSelect");
  if (!select) {
    return;
  }
  select.innerHTML = "";
  state.events.forEach((event) => {
    const option = document.createElement("option");
    option.value = event.id;
    option.textContent = `${event.name} (ID ${event.id})`;
    select.appendChild(option);
  });
}

function renderLocations() {
  const select = getEl("locationSelect");
  if (!select) {
    return;
  }
  select.innerHTML = "";
  state.locations.forEach((location) => {
    const option = document.createElement("option");
    option.value = location.id;
    option.textContent = `${location.name} (ID ${location.id})`;
    select.appendChild(option);
  });
}

function seatCardHtml(seat) {
  return `
    <div class="seat-card" data-action="open-seat" data-seat-id="${seat.id}">
      <div class="seat-number">#${seat.seatNumber}</div>
      <div class="seat-state ${seat.reserved ? "seat-reserved" : "seat-free"}">
        ${seat.reserved ? "Reserved" : "Free"}
      </div>
    </div>
  `;
}

function renderSeats() {
  const container = getEl("seatsContainer");
  if (!container) {
    return;
  }
  if (state.seats.length === 0) {
    container.innerHTML = "<p>No seats loaded.</p>";
    return;
  }
  container.innerHTML = state.seats.map(seatCardHtml).join("");
}

async function loadEvents() {
  const events = await apiRequest("/events");
  state.events = Array.isArray(events) ? events : [];
  renderEvents();
  if (getEl("locationSelect")) {
    await loadLocations();
  }
  setOutput("Loaded events", state.events);
}

async function loadLocations() {
  const eventId = selectedEventId();
  if (!eventId) {
    state.locations = [];
    renderLocations();
    return;
  }
  const locations = await apiRequest(`/events/${eventId}/locations`);
  state.locations = Array.isArray(locations) ? locations : [];
  renderLocations();
  setOutput("Loaded locations", state.locations);
}

async function loadSeats() {
  const eventId = selectedEventId();
  const locationId = selectedLocationId();
  if (!eventId || !locationId) {
    setOutput("Select event and location first.");
    return;
  }
  const seats = await apiRequest(`/events/${eventId}/locations/${locationId}/seats`);
  state.seats = Array.isArray(seats) ? seats : [];
  renderSeats();
  setOutput("Loaded seats", state.seats);
}

function selectedSeat() {
  return state.seats.find((seat) => String(seat.id) === String(state.selectedSeatId)) || null;
}

function closeSeatDialog() {
  const seatDialog = getEl("seatDialog");
  if (seatDialog?.open) {
    seatDialog.close();
  }
}

function openSeatDialog(seatId) {
  state.selectedSeatId = seatId;
  const seat = selectedSeat();
  const seatDialog = getEl("seatDialog");
  if (!seat || !seatDialog) {
    return;
  }
  getEl("seatDialogTitle").textContent = `Seat ${seat.seatNumber} (ID ${seat.id})`;
  getEl("seatDialogInfo").textContent = `Type: ${seat.type}. Status: ${seat.reserved ? "Reserved" : "Available"}.`;
  getEl("seatDialogReserveButton").textContent = seat.reserved ? "Cancel reservation" : "Reserve seat";
  seatDialog.showModal();
}

async function reserveSeat(seatId) {
  const eventId = selectedEventId();
  const locationId = selectedLocationId();
  if (!state.token) {
    setOutput("Login required to reserve seats.");
    return;
  }
  const result = await apiRequest(
    `/events/${eventId}/locations/${locationId}/seats/${seatId}/reservation`,
    { method: "POST" }
  );
  setOutput("Seat reserved", result);
  closeSeatDialog();
  await loadSeats();
}

async function cancelReservation(seatId) {
  const eventId = selectedEventId();
  const locationId = selectedLocationId();
  if (!state.token) {
    setOutput("Login required to cancel reservations.");
    return;
  }
  await apiRequest(`/events/${eventId}/locations/${locationId}/seats/${seatId}/reservation`, { method: "DELETE" });
  setOutput("Reservation removed");
  closeSeatDialog();
  await loadSeats();
}

async function onSeatDialogAction() {
  const seat = selectedSeat();
  if (!seat) {
    setOutput("Seat no longer available in list.");
    closeSeatDialog();
    return;
  }
  if (seat.reserved) {
    await cancelReservation(seat.id);
  } else {
    await reserveSeat(seat.id);
  }
}

async function onLogin(event) {
  event.preventDefault();
  const payload = {
    username: getEl("loginUsername").value.trim(),
    password: getEl("loginPassword").value
  };
  const result = await apiRequest("/auth/login", {
    method: "POST",
    body: JSON.stringify(payload)
  });
  state.token = result.token || "";
  localStorage.setItem(TOKEN_KEY, state.token);
  setAuthStatus();
  setOutput("Login successful. JWT saved to localStorage.", result);
  window.location.href = readNextPageFromQuery();
}

async function onRegister(event) {
  event.preventDefault();
  const payload = {
    username: getEl("regUsername").value.trim(),
    firstName: getEl("regFirstName").value.trim(),
    lastName: getEl("regLastName").value.trim(),
    email: getEl("regEmail").value.trim(),
    password: getEl("regPassword").value,
    phone: getEl("regPhone").value.trim(),
    dateOfBirth: getEl("regDob").value || null
  };
  const result = await apiRequest("/auth/register", {
    method: "POST",
    body: JSON.stringify(payload)
  });
  setOutput("Registration successful", result);
}

function onLogout() {
  state.token = "";
  localStorage.removeItem(TOKEN_KEY);
  setAuthStatus();
  setOutput("Logged out.");
  window.location.href = "./logged-out.html";
}

async function createEvent(event) {
  event.preventDefault();
  const payload = {
    name: getEl("eventName").value.trim(),
    description: getEl("eventDescription").value.trim(),
    date: getEl("eventDate").value
  };
  const result = await apiRequest("/events", {
    method: "POST",
    body: JSON.stringify(payload)
  });
  setOutput("Event created", result);
  await loadEvents();
}

async function deleteEvent() {
  const eventId = selectedEventId();
  if (!eventId) {
    setOutput("Select an event first.");
    return;
  }
  const result = await apiRequest(`/events/${eventId}`, { method: "DELETE" });
  setOutput("Event deleted", result);
  await loadEvents();
}

async function createLocation(event) {
  event.preventDefault();
  const eventId = selectedEventId();
  if (!eventId) {
    setOutput("Select an event first.");
    return;
  }
  const payload = { name: getEl("locationName").value.trim() };
  const result = await apiRequest(`/events/${eventId}/locations`, {
    method: "POST",
    body: JSON.stringify(payload)
  });
  setOutput("Location created", result);
  await loadLocations();
}

async function deleteLocation() {
  const eventId = selectedEventId();
  const locationId = selectedLocationId();
  if (!eventId || !locationId) {
    setOutput("Select event and location first.");
    return;
  }
  const result = await apiRequest(`/events/${eventId}/locations/${locationId}`, { method: "DELETE" });
  setOutput("Location deleted", result);
  await loadLocations();
}

async function createSeats(event) {
  event.preventDefault();
  const eventId = selectedEventId();
  const locationId = selectedLocationId();
  if (!eventId || !locationId) {
    setOutput("Select event and location first.");
    return;
  }
  const payload = {
    type: getEl("seatType").value.trim(),
    seatCount: Number(getEl("seatCount").value)
  };
  const result = await apiRequest(`/events/${eventId}/locations/${locationId}/seats`, {
    method: "POST",
    body: JSON.stringify(payload)
  });
  setOutput("Seats created", result);
}

async function runSafely(fn) {
  try {
    await fn();
  } catch (error) {
    setOutput("Request failed", { error: error.message });
  }
}

function initAuthPage() {
  const loginForm = getEl("loginForm");
  const registerForm = getEl("registerForm");
  if (loginForm) {
    loginForm.addEventListener("submit", (event) => runSafely(() => onLogin(event)));
  }
  if (registerForm) {
    registerForm.addEventListener("submit", (event) => runSafely(() => onRegister(event)));
  }
}

function initSelectionControls() {
  const refreshEventsButton = getEl("refreshEventsButton");
  const refreshLocationsButton = getEl("refreshLocationsButton");
  const eventSelect = getEl("eventSelect");

  if (refreshEventsButton) {
    refreshEventsButton.addEventListener("click", () => runSafely(loadEvents));
  }
  if (refreshLocationsButton) {
    refreshLocationsButton.addEventListener("click", () => runSafely(loadLocations));
  }
  if (eventSelect) {
    eventSelect.addEventListener("change", () => runSafely(loadLocations));
  }
}

function initSeatsPage() {
  const loadSeatsButton = getEl("loadSeatsButton");
  const seatsContainer = getEl("seatsContainer");
  const seatDialogReserveButton = getEl("seatDialogReserveButton");
  const seatDialogCloseButton = getEl("seatDialogCloseButton");

  if (loadSeatsButton) {
    loadSeatsButton.addEventListener("click", () => runSafely(loadSeats));
  }
  if (seatDialogReserveButton) {
    seatDialogReserveButton.addEventListener("click", () => runSafely(onSeatDialogAction));
  }
  if (seatDialogCloseButton) {
    seatDialogCloseButton.addEventListener("click", closeSeatDialog);
  }
  if (seatsContainer) {
    seatsContainer.addEventListener("click", (event) => {
      const target = event.target;
      if (!(target instanceof HTMLElement)) {
        return;
      }
      const seatCard = target.closest("[data-action='open-seat']");
      if (!(seatCard instanceof HTMLElement)) {
        return;
      }
      const seatId = seatCard.getAttribute("data-seat-id");
      if (seatId) {
        openSeatDialog(seatId);
      }
    });
  }
}

function initManagePage() {
  const createEventForm = getEl("createEventForm");
  const deleteEventButton = getEl("deleteEventButton");
  const createLocationForm = getEl("createLocationForm");
  const deleteLocationButton = getEl("deleteLocationButton");
  const createSeatsForm = getEl("createSeatsForm");

  if (createEventForm) {
    createEventForm.addEventListener("submit", (event) => runSafely(() => createEvent(event)));
  }
  if (deleteEventButton) {
    deleteEventButton.addEventListener("click", () => runSafely(deleteEvent));
  }
  if (createLocationForm) {
    createLocationForm.addEventListener("submit", (event) => runSafely(() => createLocation(event)));
  }
  if (deleteLocationButton) {
    deleteLocationButton.addEventListener("click", () => runSafely(deleteLocation));
  }
  if (createSeatsForm) {
    createSeatsForm.addEventListener("submit", (event) => runSafely(() => createSeats(event)));
  }
}

function initSharedControls() {
  const logoutButton = getEl("logoutButton");
  if (logoutButton) {
    logoutButton.addEventListener("click", () => {
      if (state.token) {
        onLogout();
      } else {
        window.location.href = "./login.html";
      }
    });
  }
}

function updateAuthActionButton() {
  const logoutButton = getEl("logoutButton");
  if (!logoutButton) {
    return;
  }
  logoutButton.textContent = state.token ? "Logout" : "Login";
}

function updateAuthStateClass() {
  document.documentElement.classList.toggle("is-authenticated", Boolean(state.token));
}

function updateAuthLinks() {
  const isAuthenticated = Boolean(state.token);
  document.querySelectorAll('a[href*="login.html"], a[href*="register.html"]').forEach((link) => {
    link.hidden = isAuthenticated;
  });
}

function bootstrap() {
  if (isProtectedPage() && !state.token) {
    redirectToLogin();
    return;
  }

  if ((currentPage === "login.html" || currentPage === "register.html") && state.token) {
    window.location.href = "./seats.html";
    return;
  }

  initializeBaseUrlInput();
  setAuthStatus();
  initSharedControls();
  initAuthPage();
  initSelectionControls();
  initSeatsPage();
  initManagePage();

  if (getEl("eventSelect")) {
    runSafely(loadEvents);
  }
}

bootstrap();
