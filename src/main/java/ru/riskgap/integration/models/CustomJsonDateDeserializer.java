package ru.riskgap.integration.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Date converter from JSON-string to {@link Date}
 * @author andrey
 */
public class CustomJsonDateDeserializer extends JsonDeserializer<Date> {
    private final static String STRING_DATE_FORMAT = "dd.MM.yyyy";
    public final static DateFormat DATE_FORMATTER = new SimpleDateFormat(STRING_DATE_FORMAT);

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String date = jsonParser.getText();
        try {
            return DATE_FORMATTER.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
