package ru.riskgap.integration.exceptions;

/**
 * Created by andrey on 24.07.15.
 */
public class AuthException extends AbstractException {
    public AuthException(String parameter) {
        super("Authorization failed", "Parameter '"+parameter+"' is incorrect", 401);
    }
}
