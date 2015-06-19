package ru.riskgap.integration.models;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.io.Serializable;

public class RestToken extends AbstractAuthenticationToken implements Serializable {

    private String username;
    private String token;

    public RestToken(String username, String token) {
        super(null);
        super.setAuthenticated(true);
        this.username = username;
        this.token = token;
    }

    //everything that approves a user - a token is our case
    @Override
    public Object getCredentials() {
        return token;
    }


    //everything that identifies a user - a username in our case
    //Many users can use the same token
    @Override
    public Object getPrincipal() {
        return username;
    }

}
