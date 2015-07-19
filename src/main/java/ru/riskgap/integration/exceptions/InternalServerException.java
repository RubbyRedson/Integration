package ru.riskgap.integration.exceptions;

/**
 * Created by andrey on 19.07.15.
 */
public class InternalServerException extends AbstractException {
    public InternalServerException(String detail) {
        super("Internal server error", detail, 500);
    }
}
