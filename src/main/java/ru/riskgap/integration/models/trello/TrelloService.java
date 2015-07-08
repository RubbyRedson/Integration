package ru.riskgap.integration.models.trello;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrey on 06.07.15.
 */
//TODO: Add error handler of trello responses
public class TrelloService {

    private final Logger log = LoggerFactory.getLogger(TrelloService.class);

    private static final String BASE_URL = "https://api.trello.com/1/";
    private static final String GET_LISTS_OF_BOARD = "boards/{0}/lists";
    private static final String GET_CARD_OF_BOARD = "boards/{0}/cards/{1}";
    private static final String GET_LIST_BY_ID = "lists/{0}";
    private static final HashMap<Task.Status, String> STATUS_LIST_MAP = new HashMap<Task.Status, String>() {
        {
            put(Task.Status.OPEN, "To Do");
            put(Task.Status.CLOSED, "Done");
        }
    };
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    public TrelloService(HttpClient httpClient) {
        this.httpClient = httpClient;
        objectMapper = new ObjectMapper();
    }

    /**
     * Get list (column) of board for the task by its status
     *
     * @param status    status of a task
     * @param boardId   id of a board where new task will be created
     * @param appKey    application key
     * @param userToken token of the user, who has access to the board
     * @return id of a list
     * @throws IOException
     */
    public String getListIdByStatus(Task.Status status, String boardId, String appKey, String userToken) throws IOException {
        try {
            String withoutParams = MessageFormat.format(BASE_URL + GET_LISTS_OF_BOARD, boardId);
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .build().toString();
            log.info("getListIdByStatus, URL: {}", url);
            CloseableHttpResponse response = httpClient.get(url);
            String entity = httpClient.extractEntity(response, true);
            JsonNode jsonNode = objectMapper.readTree(entity);
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    String listName = node.get("name").asText();
                    if (listName.equals(STATUS_LIST_MAP.get(status)))
                        return node.get("id").asText();
                }
            }
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }

    public Task getTaskDetails(String containerId, String taskId, String appKey, String userToken) throws IOException {
        //TODO: validate fields in task
        try {
            String withoutParams = MessageFormat.format(BASE_URL + GET_CARD_OF_BOARD,
                    containerId, taskId);
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .build().toString();
            log.info("getTaskDetails, URL: {}", url);
            CloseableHttpResponse response = httpClient.get(url);
            String entity = httpClient.extractEntity(response, true);
            //  objectMapper.readTree(entity).


        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }

    public Task parseCardInTask(String cardJson, String appKey, String userToken) throws IOException, ParseException {
        JsonNode root = objectMapper.readTree(cardJson);
        Task task = new Task();
        task.setTargetSystem(Task.TargetSystem.TRELLO);
        task.setTaskId(root.get("id").asText());
        task.setContainerId(root.get("idBoard").asText());
        task.setName(root.get("name").asText());
        task.setDescription(root.get("desc").asText());
        task.setDue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(root.get("due").asText()));
        task.setUserId(root.get("actions").get(0).get("memberCreator").get("id").asText()); //from action "createCard"
        task.setAssigneeId(root.get("idMembers").get(0).asText()); //only first member is assigned for the task
        task.setRiskRef(root.get("attachments").get(0).get("url").asText()); //link to risk in RiskGap
        task.setStatus(getStatusByList(root.get("idList").asText(),appKey, userToken));
        return task;
    }

    public Task.Status getStatusByList(String listId, String appKey, String userToken) throws IOException {
        String withoutParams = MessageFormat.format(BASE_URL + GET_LIST_BY_ID, listId);
        try {
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("userToken", userToken)
                    .build().toString();
            CloseableHttpResponse response = httpClient.get(url);
            String entity = httpClient.extractEntity(response, true);
            JsonNode root = objectMapper.readTree(entity);
            for (Map.Entry<Task.Status, String> entry : STATUS_LIST_MAP.entrySet()) {
                if (entry.getValue().equals(root.get("name").asText()))
                    return entry.getKey();
            }
            //TODO: Own exception
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        throw new IllegalArgumentException("Can't recognize status of tasks in the list with id=" + listId);
    }


}
