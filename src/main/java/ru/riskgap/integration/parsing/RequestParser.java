package ru.riskgap.integration.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.riskgap.integration.models.TargetSystemEnum;
import ru.riskgap.integration.models.Task;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Receives the request body as json and parses it to create a Task object that holds all the received information
 * Created by Nikita on 16.06.2015.
 */
@Component
public class RequestParser {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public Task parse(String jsonBody) throws IOException, ParseException {

        Map<String,String> jsonMap = objectMapper.readValue(jsonBody, HashMap.class);
        if (logger.isInfoEnabled())
            logger.info("Received json body map is " + jsonMap);

        Task result = new Task();

        result.setName(jsonMap.get(Task.TASK_NAME));
        result.setStatus(jsonMap.get(Task.TASK_STATUS));
        result.setDescription(jsonMap.get(Task.TASK_DESCRIPTION));
        result.setUserId(jsonMap.get(Task.USER_ID));
        result.setUsername(jsonMap.get(Task.USERNAME));
        result.setUserEmail(jsonMap.get(Task.USER_EMAIL));
        result.setAssigneeId(jsonMap.get(Task.ASSIGNEE_ID));
        result.setAssigneeUsername(jsonMap.get(Task.ASSIGNEE_USERNAME));
        result.setAssigneeEmail(jsonMap.get(Task.ASSIGNEE_EMAIL));
        String date = jsonMap.get(Task.TASK_DUE);
        if (date != null) {
            result.setDue(Task.DATE_FORMATTER.parse(date));
        }
        result.setRiskRef(jsonMap.get(Task.RISK_REF));
        String targetSystem = jsonMap.get(Task.TARGET_SYSTEM);
        if (targetSystem != null) {
            result.setTargetSystem(TargetSystemEnum.valueOf(targetSystem));
        }

        return result;
    }
}