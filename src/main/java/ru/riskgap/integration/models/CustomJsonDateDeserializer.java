package ru.riskgap.integration.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.riskgap.integration.exceptions.InvalidInputDataException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.riskgap.integration.exceptions.InvalidInputDataException.Reason.INCORRECT;

/**
 * Date converter from JSON-string to {@link Date}
 *
 * @author andrey
 */
public class CustomJsonDateDeserializer extends JsonDeserializer<Date> {
    private final static String STRING_DATE_FORMAT = "dd.MM.yyyy";
    private final static String STRING_DATE_FORMAT_WITH_TIME = "dd.MM.yyyy HH:mm";
    public final static DateFormat DATE_FORMATTER = new SimpleDateFormat(STRING_DATE_FORMAT);
    public final static DateFormat DATE_FORMATTER_WITH_TIME = new SimpleDateFormat(STRING_DATE_FORMAT_WITH_TIME);

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String date = jsonParser.getText();
        try {
            return DATE_FORMATTER_WITH_TIME.parse(date);
        } catch (ParseException e) {
            try {
                return DATE_FORMATTER.parse(date);
            } catch (ParseException e1) {
                throw new InvalidInputDataException("due", INCORRECT);
            }
        }

    }
}
