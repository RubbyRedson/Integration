package ru.riskgap.integration.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.riskgap.integration.exceptions.InvalidInputDataException;
import ru.riskgap.integration.models.Task;

import java.io.IOException;

import static ru.riskgap.integration.exceptions.InvalidInputDataException.Reason.INCORRECT;

/**
 * Receives the request body as json and parses it to create a Task object that holds all the received information
 * Created by Nikita on 16.06.2015.
 */
public class RequestConverter {
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(RequestConverter.class);

    public RequestConverter() {
        this(new ObjectMapper());
    }

    public RequestConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * @param jsonBody of a request from initiating server. The task created holds info for creating/updating task
     * @return Task instance with all of the information from jsonBody
     * @throws IOException when input json is malformed
     */
    public Task fromJSONtoTask(String jsonBody) throws Exception {
        Task result;
        try {
            logger.info("[Converter] From JSON to Task");
            result = objectMapper.readValue(jsonBody, Task.class);
        } catch (JsonMappingException | JsonParseException ex) {
            throw new InvalidInputDataException(INCORRECT);
        }
        return result;
    }

    public String fromTaskToJSON(Task task) throws Exception {
        logger.info("[Converter] From Task to JSON");
        String json = objectMapper.writeValueAsString(task);
        return json;
    }
}
