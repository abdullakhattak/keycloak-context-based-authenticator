# Keycloak Context-Based Authenticator

This project implements a custom Keycloak authenticator that allows or denies access based on the IP address of the incoming login request. It can be integrated into a Keycloak authentication flow to enforce context-aware security decisions.

## Features

- Validates incoming IP against a configurable allowlist.
- Supports both IPv4 and IPv6 loopback addresses (`127.0.0.1`, `::1`).
- Easily pluggable into any Keycloak authentication flow.
- Provides detailed logging for auditing and troubleshooting.

## Project Structure

context-based-authenticator/
├── src/
│ └── main/
│ ├── java/
│ │ └── com/example/ContextBasedAuthenticator.java
│ └── resources/
├── target/
├── pom.xml
├── README.md
└── .gitignore


## Prerequisites

- JDK 11 or later
- Maven 3.x
- Keycloak 21+ (tested on 26.2.5)
- Basic familiarity with Keycloak authentication flows


## Usage

1. **Build the project with Maven**

   Navigate to the project root and run:
   mvn clean package

2. **Deploy the JAR to Keycloak**

Copy the compiled JAR file from the target/ directory into the providers/ folder of your Keycloak installation:
cp target/context-based-authenticator.jar /opt/keycloak/providers/

3. **Restart Keycloak**

Restart the Keycloak server to load the new custom authenticator:

bin/kc.sh build
bin/kc.sh start

4. **Register the Authenticator in the Admin Console**

Go to Authentication → Flows.

Duplicate the Browser flow or create a new flow.

Add your custom authenticator as an execution step (ensure it runs after a user-identifying step like "Username Password Form").

5. **Update Allowed IPs**

Open ContextBasedAuthenticator.java and update the IP allowlist:

List<String> allowedIps = Arrays.asList("127.0.0.1", "::1", "192.168.0.103");

Rebuild and redeploy the JAR after changes.


## Logs
Logs will appear in the Keycloak console with tags like:

ContextBasedAuthenticator: Received login attempt from IP: ...
ContextBasedAuthenticator: IP ... is NOT allowed. Denying access.
