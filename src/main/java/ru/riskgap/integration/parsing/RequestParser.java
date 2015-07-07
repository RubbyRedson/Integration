package ru.riskgap.integration.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.riskgap.integration.models.CustomJsonDateDeserializer;
import ru.riskgap.integration.models.TargetSystemEnum;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.models.tfs.TfsRequestBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public Task parse(String jsonBody) {
        Task task = null;
        try {
            task = objectMapper.readValue(jsonBody, Task.class);
        } catch (IOException e) {
            logger.error("Error while parsing JSON representation of a Task", e);
        }
        return task;
    }


    /**
     * @param jsonBody of a request from initiating server. The task created holds info for creating/updating task
     * @return Task instance with all of the information from jsonBody
     * @throws IOException when input json is malformed
     * @throws ParseException when Date in json is of incorrect format. The proper one is dd.MM.yyyy
     */
    @Deprecated
    public Task parseInputJson(String jsonBody) throws IOException, ParseException {

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
            result.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse(date));
        }
        result.setRiskRef(jsonMap.get(Task.RISK_REF));
        String targetSystem = jsonMap.get(Task.TARGET_SYSTEM);
        if (targetSystem != null) {
            result.setTargetSystem(TargetSystemEnum.valueOf(targetSystem));
        }

        return result;
    }

    /**
     * @param jsonBody of a TFS response when GET for Work item fields is executed. It converts all of it into a Task instance.
     * @return Task instance with all of the information from jsonBody
     * @throws IOException when input json is malformed
     */
    public Task parseTfsGetWorkItemFieldsResponseJson(String jsonBody) throws IOException {

        Map<String, Object> jsonOuterMap = objectMapper.readValue(jsonBody, HashMap.class);
        if (logger.isInfoEnabled())
            logger.info("Received json outer body map is " + jsonOuterMap);

        Task result = new Task();

        if (jsonOuterMap == null)
            return result;

        ArrayList<Map> jsonValueList = (ArrayList) jsonOuterMap.get("value");
        if (logger.isInfoEnabled())
            logger.info("Received json value list is " + jsonValueList);

        if (jsonValueList == null || jsonValueList.size() == 0)
            return result;

        Map<String, String> jsonMap = (Map<String, String>) jsonValueList.get(0).get("fields");
        if (logger.isInfoEnabled())
            logger.info("Received json fields map is " + jsonMap);

        if (jsonMap == null)
            return result;

        result.setName(jsonMap.get(TfsRequestBuilder.TASK_NAME));
        result.setStatus(jsonMap.get(TfsRequestBuilder.TASK_STATE));
        result.setDescription(jsonMap.get(TfsRequestBuilder.TASK_DESCR));
        String user = jsonMap.get(TfsRequestBuilder.CHANGED_BY);

        if (user != null && !user.isEmpty()) {
            String username = user.substring(0, user.lastIndexOf('<') - 1);
            String mail = user.substring(username.length());
            result.setUsername(username);
            result.setUserEmail(mail.substring(2, mail.length() - 1)); //remove < and >
        }

        String assignee = jsonMap.get(TfsRequestBuilder.TASK_ASSIGNEE);
        if (assignee != null && !assignee.isEmpty()) {
            String assigneeName = assignee.substring(0, assignee.lastIndexOf('<') - 1);
            String assigneeMail = assignee.substring(assigneeName.length());
            result.setAssigneeUsername(assigneeName);
            result.setAssigneeEmail(assigneeMail.substring(2, assigneeMail.length() - 1)); //remove < and >
        }

//        String date = jsonMap.get(Task.TASK_DUE);
//        if (date != null) {
//            result.setDue(Task.DATE_FORMATTER.parse(date));
//        }

//        result.setRiskRef(jsonMap.get(Task.RISK_REF));

        result.setTargetSystem(TargetSystemEnum.TFS);

        return result;

    }


    /**
     * @param jsonBody of a TFS response when GET for Work Item history is executed. It converts all of it into a Task instance.
     * @return Task instance with all of the information from jsonBody
     * @throws IOException when input json is malformed
     */
    public Task parseTfsGetWorkItemHistoryResponseJson(String jsonBody) throws IOException {
         return null;
    }
}
