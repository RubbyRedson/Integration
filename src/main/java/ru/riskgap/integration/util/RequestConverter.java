package ru.riskgap.integration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.riskgap.integration.models.Task;

import java.io.IOException;

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
    public Task fromJSONtoTask(String jsonBody) throws IOException {
        Task result = objectMapper.readValue(jsonBody, Task.class);
        if (logger.isInfoEnabled())
            logger.info("Received new " + result);
        return result;
    }

    public String fromTaskToJSON(Task task) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(task);
        if (logger.isInfoEnabled())
            logger.info("Send new " + json);
        return json;
    }
}
