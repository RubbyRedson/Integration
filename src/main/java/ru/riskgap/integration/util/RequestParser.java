package ru.riskgap.integration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.riskgap.integration.models.Task;

import java.io.IOException;

/**
 * Receives the request body as json and parses it to create a Task object that holds all the received information
 * Created by Nikita on 16.06.2015.
 */
@Component
public class RequestParser {
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public RequestParser() {
        this(new ObjectMapper());
    }

    public RequestParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * @param jsonBody of a request from initiating server. The task created holds info for creating/updating task
     * @return Task instance with all of the information from jsonBody
     * @throws IOException when input json is malformed
     */
    public Task parse(String jsonBody) throws IOException {
        Task result = objectMapper.readValue(jsonBody, Task.class);
        if (logger.isInfoEnabled())
            logger.info("Received new " + result);
        return result;
    }
}
