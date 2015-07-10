package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrey on 06.07.15.
 */
//TODO: Add error handler of trello responses
public class TrelloService {

    private final Logger log = LoggerFactory.getLogger(TrelloService.class);
    private final static SimpleDateFormat TRELLO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final String BASE_URL = "https://api.trello.com/1/";
    private static final String GET_LISTS_OF_BOARD = "boards/{0}/lists";
    private static final String GET_CARD_BY_ID = "cards/{0}";
    private static final String GET_LIST_BY_ID = "lists/{0}";
    private static final String POST_CARD = "cards";
    private static final String POST_COMMENT = "cards/{0}/actions/comments";
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
     * @param boardId   id of a board with lists
     * @param appKey    application key
     * @param userToken token of the user, who has access to the board
     * @return id of a list
     * @throws IOException
     */
    String getListIdByStatus(Task.Status status, String boardId, String appKey, String userToken) throws IOException {
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

    /**
     * Gets Task details by card ID in Trello
     *
     * @param cardId    ID of card in Trello
     * @param appKey    application key
     * @param userToken token of the user, who has access to the board
     * @return instance of Task with information of card from Trello
     * @throws IOException
     * @throws ParseException
     */
    public Task getTaskByCardId(String cardId, String appKey, String userToken) throws IOException, ParseException {
        try {
            String withoutParams = MessageFormat.format(BASE_URL + GET_CARD_BY_ID, cardId);
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .build().toString();
            log.info("getTaskByCardId, URL: {}", url);
            CloseableHttpResponse response = httpClient.get(url);
            String entity = httpClient.extractEntity(response, true);
            return parseCardInTask(entity, appKey, userToken);
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }

    /**
     * Creates card in Trello using Task object and fills its id and ids of comments
     *
     * @param task      data which be used mapped to Trello card
     * @param appKey    application key
     * @param userToken token of the user, who has access to the board
     * @return task object with filled task id and ids of comments
     * @throws IOException
     */
    public Task createCardByTask(Task task, String appKey, String userToken) throws IOException {
        task.setTaskId(createTaskOnly(task, appKey, userToken));
        List<Comment> comments = task.getComments();
        if (comments !=null) {
            for (Comment comment : comments) {
                comment.setCommentId(addComment(task.getTaskId(), comment, appKey, userToken));
            }
        }
        return task;
    }

    String createTaskOnly(Task task, String appKey, String userToken) throws IOException {
        String withoutParams = BASE_URL + POST_CARD;
        try {
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .addParameter("name", task.getName())
                    .addParameter("desc", task.getDescription())
                    .addParameter("idList", task.getContainerId())
                    .addParameter("due", TRELLO_DATE_FORMAT.format(task.getDue()))
                    .addParameter("urlSource", task.getRiskRef())
                    .build().toString();
            log.info("createTaskOnly, URL: {}", url);
            CloseableHttpResponse createCardResponse = httpClient.post(url, null);
            String entity = httpClient.extractEntity(createCardResponse, true);
            return objectMapper.readTree(entity).get("id").asText();
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }

    String addComment(String taskId, Comment comment, String appKey, String userToken) throws IOException {
        String withoutParams = MessageFormat.format(BASE_URL + POST_COMMENT, taskId);
        try {
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .addParameter("text", comment.getText())
                    .build().toString();
            log.info("addComment, URL: {}", url);
            CloseableHttpResponse createCommentResponse = httpClient.post(url, null);
            String entity = httpClient.extractEntity(createCommentResponse, true);
            return objectMapper.readTree(entity).get("id").asText();
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }

    Task parseCardInTask(String cardJson, String appKey, String userToken) throws IOException, ParseException {
        JsonNode root = objectMapper.readTree(cardJson);
        Task task = new Task();
        task.setTargetSystem(Task.TargetSystem.TRELLO);
        task.setTaskId(root.get("id").asText());
        task.setContainerId(root.get("idBoard").asText());
        task.setName(root.get("name").asText());
        task.setDescription(root.get("desc").asText());
        task.setDue(TRELLO_DATE_FORMAT.parse(root.get("due").asText()));
        task.setUserId(getIdCreatorFromActions(root.get("actions"))); //from action "createCard"
        task.setAssigneeId(root.get("idMembers").get(0).asText()); //only first member is assigned for the task
        task.setRiskRef(getLinkFromAttachments(root.get("attachments")));

        task.setStatus(getStatusByList(root.get("idList").asText(), appKey, userToken));
        return task;
    }

    private String getLinkFromAttachments(JsonNode attachmentsArray) throws IOException {
        log.info("attachments are array? {}", attachmentsArray.isArray());
        for (JsonNode attachment : attachmentsArray) {
            String name = attachment.get("name").asText();
            if (name.startsWith("http"))
                return name; //for links 'name' is URL
        }
        return null;
    }

    private String getIdCreatorFromActions(JsonNode actionsArray) {
        log.info("actions are array? {}", actionsArray.isArray());
        for (JsonNode action : actionsArray) {
            if (action.get("type").asText().equals("createCard"))
                return action.get("memberCreator").get("id").asText();
        }
        return null;
    }


    /**
     * Gets status of tasks in the given list by using "list name" <=> "status" conversion.
     *
     * @param listId    id of the list in Trello
     * @param appKey    Trello application key
     * @param userToken token of a Trello user
     * @return status of tasks in the given list
     * @throws IOException
     */
    Task.Status getStatusByList(String listId, String appKey, String userToken) throws IOException {
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
