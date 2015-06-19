package ru.riskgap.integration.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Usually {@link org.springframework.security.authentication.AuthenticationManager} is used to check one type of {@link org.springframework.security.core.Authentication} (e.g. username and password) to give another type of {@link org.springframework.security.core.Authentication} (e.g. personal token). But in this case we don't need any transformation.
 */

@Component
public class NoOpAuthManager implements AuthenticationManager {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authentication;
    }
}
