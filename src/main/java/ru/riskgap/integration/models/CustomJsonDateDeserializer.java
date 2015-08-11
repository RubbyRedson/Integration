package ru.riskgap.integration.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.riskgap.integration.exceptions.InvalidInputDataException;

import java.io.IOException;
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
    final static String STRING_DATE_FORMAT = "dd.MM.yyyy";
    final static String STRING_DATE_FORMAT_WITH_TIME = "dd.MM.yyyy HH:mm";
    public final static ThreadLocal<SimpleDateFormat> DATE_FORMATTER = new ThreadLocal<>();
    public final static ThreadLocal<SimpleDateFormat> DATE_FORMATTER_WITH_TIME = new ThreadLocal<>();

//    static {
//        DATE_FORMATTER.set(new SimpleDateFormat(STRING_DATE_FORMAT));
//        DATE_FORMATTER_WITH_TIME.set(new SimpleDateFormat(STRING_DATE_FORMAT_WITH_TIME));
//    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String date = jsonParser.getText();
        try {
            return parseDate(date);
        } catch (ParseException e) {
            throw new InvalidInputDataException("due", INCORRECT);
        }
    }

    public static Date parseDate(String date) throws ParseException {
        if (DATE_FORMATTER.get() == null) {
            DATE_FORMATTER.set(new SimpleDateFormat(STRING_DATE_FORMAT));
        }
        if (DATE_FORMATTER_WITH_TIME.get() == null) {
            DATE_FORMATTER_WITH_TIME.set(new SimpleDateFormat(STRING_DATE_FORMAT_WITH_TIME));
        }
        try {
            return DATE_FORMATTER_WITH_TIME.get().parse(date);
        } catch (ParseException e) {
                return DATE_FORMATTER.get().parse(date);
        }
    }




}
