
package com.example;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ConfiguredProvider;

import java.util.Collections;
import java.util.List;


public class ContextBasedAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "context-based-authenticator";

    @Override
    public String getDisplayType() {
        return "Context Based Authenticator";
    }

    @Override
    public String getReferenceCategory() {
        return "context-based";
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[]{
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.DISABLED
        };
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return new ContextBasedAuthenticator();
    }

    @Override
    public void init(Scope config) {
        // Initialization logic if needed
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // Post-initialization logic if needed
    }

    @Override
    public void close() {
        // Cleanup if needed
    }

    @Override
    public String getHelpText() {
        return "Custom authenticator based on request context.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
    return Collections.emptyList();
    }
    
    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
