# LAN seat reservation platform
### (PWP SPRING 2026)
## Group information
* Ville Kujala (ville.kujala@student.oulu.fi)
* Kalle Asmundi (kalle.asmundi@student.oulu.fi)
* Eemeli Kyröläinen (eemeli.kyrolainen@student.oulu.fi)


__Remember to include all required documentation and HOWTOs, including how to create and populate the database, how to run and test the API, the url to the entrypoint, instructions on how to setup and run the client, instructions on how to setup and run the axiliary service and instructions on how to deploy the api in a production environment__

## Deployment
### Requirements
- Docker
- A registered domain name
- Telegram bot token
- Ports 80 and 443 accessible from the internet

### Instructions
All instructions assume that the application is being run on a Linux platform. To start, clone the repository with:
```
git clone https://github.com/DatLem0n/pwp-lan-seat-reservation.git
```

Before running the application create a **.env** file according to the example and fill it out. Also change **"yourdomain.com"** to your actual domain in both Nginx configuration files. All commands are run in the project root.

### Acquiring certificates
A TLS certificate is needed for the application to be able to start. To get the certificate for the first time run:
```
docker compose run -d --rm \
  -p 80:80 \
  -v ./nginx/nginx.bootstrap.conf:/etc/nginx/conf.d/default.conf:ro \
  -v certbot_www:/var/www/certbot \
  nginx
```
Make sure to change **"yourdomain.com"** and **email** to your actual ones.
```
docker compose run --rm certbot certonly \
  --webroot \
  --webroot-path /var/www/certbot \
  -d yourdomain.com \
  --email you@yourdomain.com \
  --agree-tos \
  --no-eff-email
```

### Getting a Telegram bot token
Follow instructions here to create your own Telegram bot and to acquire a bot token https://core.telegram.org/bots/tutorial

### Starting the application stack
To start the application stack run:
```
docker compose up
```
Wait for the stack to come up. 

To start the stack in the background run:
```
docker compose up -d
```

### Accessing the application
- WebUI: "yourdomain.com"  
- API base URL: "yourdomain.com/api".

### Running tests
Tests utilize Spring Mock HTTP requests, so only the database service is required to be active.
Easiest way to do this is with:
```
docker compose up -d mariadb
```
or alternatively you could start the full application stack with:
```
docker compose up -d
```

After database is running, navigate inside `seat-reservation` and ensure the maven has execution permission:
```
cd seat-reservation
```
```
chmod +x mvnw
```
Now to start tests run ensure that you have *JDK 25* installed and configured and then run:
```
./mvnw test
```
**Review results**
*   **Console:** Summary of passes and failures will appear directly in your terminal.
*   **Reports:** Detailed reports and code coverage metrics are generated at:
`seat-reservation/target/site/jacoco/index.html` (or `target/site/index.html`).