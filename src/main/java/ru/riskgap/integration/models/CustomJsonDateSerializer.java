package ru.riskgap.integration.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.riskgap.integration.models.CustomJsonDateDeserializer.DATE_FORMATTER_WITH_TIME;
import static ru.riskgap.integration.models.CustomJsonDateDeserializer.STRING_DATE_FORMAT_WITH_TIME;

/**
 * Created by andrey on 11.07.15.
 */
public class CustomJsonDateSerializer extends JsonSerializer<Date>{
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(formatDate(date));
    }

    public static String formatDate(Date date) {
        if (DATE_FORMATTER_WITH_TIME.get() == null) {
            DATE_FORMATTER_WITH_TIME.set(new SimpleDateFormat(STRING_DATE_FORMAT_WITH_TIME));
        }
        return DATE_FORMATTER_WITH_TIME.get().format(date);
    }
}
