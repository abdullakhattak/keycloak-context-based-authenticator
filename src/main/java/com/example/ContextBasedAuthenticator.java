package com.example;

import java.util.List;
import java.util.Arrays;

import jakarta.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.UserModel;

public class ContextBasedAuthenticator implements Authenticator {

    private static final Logger logger = Logger.getLogger(ContextBasedAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        // Defensive check: ensure user is set in context before proceeding
        if (context.getUser() == null) {
            logger.warn("ContextBasedAuthenticator: user is null in authentication context. Failing authentication.");
            context.failure(AuthenticationFlowError.INVALID_USER);
            return;
        }

        String clientIp = context.getConnection().getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            clientIp = "127.0.0.1";  // Normalize IPv6 to IPv4 for local dev
        }

        // Log the incoming IP address
        logger.infof("ContextBasedAuthenticator: Received login attempt from IP: %s for user: %s", clientIp, context.getUser().getUsername());

        // Define list of allowed IPs
        List<String> allowedIps = Arrays.asList("192.168.0.103", "127.0.0.1", "::1");

        // Check if IP is in allowed list
        if (allowedIps.contains(clientIp)) {
            logger.infof("ContextBasedAuthenticator: IP %s is allowed. Proceeding with login for user %s.", clientIp, context.getUser().getUsername());
            context.success();
        } else {
            logger.warnf("ContextBasedAuthenticator: IP %s is NOT allowed. Denying access for user %s.", clientIp, context.getUser().getUsername());
            context.failure(AuthenticationFlowError.ACCESS_DENIED,
                context.form()
                       .setError("Access denied: IP " + clientIp + " is not allowed.")
                       .createErrorPage(Response.Status.FORBIDDEN));
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // No additional action needed here
    }

    @Override
    public boolean requiresUser() {
        // Return true because this authenticator expects user to be set before execution
        return true;
    }

    @Override
    public boolean configuredFor(org.keycloak.models.KeycloakSession session,
                                 org.keycloak.models.RealmModel realm,
                                 UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(org.keycloak.models.KeycloakSession session,
                                   org.keycloak.models.RealmModel realm,
                                   UserModel user) {
        // No required actions for the user
    }

    @Override
    public void close() {
        // No cleanup needed
    }
}