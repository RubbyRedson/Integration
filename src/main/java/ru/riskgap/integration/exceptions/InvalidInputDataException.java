package ru.riskgap.integration.exceptions;

import java.text.MessageFormat;

/**
 * Base exception when input message has incorrect or missed parameter
 * @author andrey
 */
public class InvalidInputDataException extends AbstractException {
    public enum Reason {
        INCORRECT, MISSED
    }

    public InvalidInputDataException(String parameter, Reason reason) {
        super("Invalid input data", MessageFormat.format("Parameter {0} is {1}", "'"+parameter+"'", reason.toString().toLowerCase()),400);
    }
}
