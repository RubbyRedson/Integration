package ru.riskgap.integration.exceptions;

import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * Base exception when input message has incorrect or missed parameter
 *
 * @author andrey
 */
public class InvalidInputDataException extends AbstractException {
    public enum Reason {
        INCORRECT, MISSED
    }

    public InvalidInputDataException(String customMessage) {
        super("Invalid input data", customMessage, 400);
    }

    public InvalidInputDataException(String parameter, Reason reason) {
        super("Invalid input data",
                MessageFormat.format("Parameter {0} is {1}", "'" + parameter + "'", reason.toString().toLowerCase()),
                400);
    }

    public InvalidInputDataException(Reason reason) {
        super("Invalid input data",
                MessageFormat.format("One of parameters is {0}", reason.toString().toLowerCase()),
                400);
    }

    public InvalidInputDataException(Reason reason, String... parameters) {
        super("Invalid input data",
                MessageFormat.format("Parameter {0} is {1}",
                        StringUtils.arrayToDelimitedString(quoteParameters(parameters), " or "), reason.toString().toLowerCase()),
                400);

    }

    private static String[] quoteParameters(String[] parameters) {
        String[] quoted = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            quoted[i] = "'" + parameters[i] + "'";
        }
        return quoted;
    }
}
