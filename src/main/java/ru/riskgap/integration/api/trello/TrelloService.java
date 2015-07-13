package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.riskgap.integration.models.Auth;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.CommentBuilder;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by andrey on 06.07.15.
 */
//TODO: Add error handler of trello responses
public class TrelloService {

    public final static SimpleDateFormat TRELLO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final Logger log = LoggerFactory.getLogger(TrelloService.class);
    private static final String BASE_URL = "https://api.trello.com/1/";
    private static final String GET_LISTS_OF_BOARD = "boards/{0}/lists";
    private static final String GET_OR_CHANGE_CARD_BY_ID = "cards/{0}";
    private static final String GET_LIST_BY_ID = "lists/{0}";
    private static final String POST_CARD = "cards";
    private static final String POST_COMMENT = "cards/{0}/actions/comments";
    private static final String CHANGE_COMMENT = "cards/{0}/actions/{1}/comments";
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
    @Deprecated
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
    @Deprecated
    public Task getTaskByCardId(String cardId, String appKey, String userToken) throws IOException, ParseException {
        try {
            String withoutParams = MessageFormat.format(BASE_URL + GET_OR_CHANGE_CARD_BY_ID, cardId);
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .addParameter("attachments", String.valueOf(true))
                    .addParameter("actions", "createCard,commentCard")
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

    @Deprecated
    public Task saveCard(Task task, String appKey, String userToken) throws IOException {
        if (task.getTaskId() == null)
            return createCard(task, appKey, userToken);
        //TODO: updateCard
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
    @Deprecated
    public Task createCard(Task task, String appKey, String userToken) throws IOException {
        task.setTaskId(createTaskOnly(task, appKey, userToken));
        List<Comment> comments = task.getComments();
        if (comments != null) {
            for (Comment comment : comments) {
                comment.setCommentId(addComment(task.getTaskId(), comment, appKey, userToken));
            }
        }
        return task;
    }

    @Deprecated
    public Task updateCard(Task task, String appKey, String userToken) throws IOException, URISyntaxException, ParseException {
        task.setTaskId(updateCardOnly(task, appKey, userToken)); //id doesn't change
        List<Comment> newComments = new ArrayList<>();
        newComments.addAll(task.getComments()); //make copy
        List<Comment> currentComments = getCommentsByCard(task.getTaskId(), appKey, userToken);
        for (Comment newComment : newComments) {
            if (newComment.getCommentId() == null) {
                addComment(task.getTaskId(), newComment, appKey, userToken);
            } else {
                Comment currentComment = findCommentById(newComment.getCommentId(), currentComments);

            }
        }
        //under construction
        return null;
    }

    @Deprecated
    private Comment findCommentById(String commentId, Collection<Comment> comments) {
        for (Comment comment : comments) {
            if (commentId.equals(comment.getCommentId()))
                return comment;
        }
        return null;
    }

    @Deprecated
    String createTaskOnly(Task task, String appKey, String userToken) throws IOException {
        String withoutParams = BASE_URL + POST_CARD;
        try {
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .addParameter("name", task.getName())
                    .addParameter("desc", task.getDescription())
                    .addParameter("idList",
                            getListIdByStatus(task.getStatus(), task.getContainerId(), appKey, userToken))
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

    @Deprecated
    String updateCardOnly(Task task, String appKey, String userToken) throws IOException {
        String withoutParams = MessageFormat.format(BASE_URL + GET_OR_CHANGE_CARD_BY_ID, task.getTaskId());
        //TODO: can link to a risk be changed?
        try {
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .addParameter("name", task.getName())
                    .addParameter("desc", task.getDescription())
                    .addParameter("idList",
                            getListIdByStatus(task.getStatus(), task.getContainerId(), appKey, userToken))
                    .addParameter("due", TRELLO_DATE_FORMAT.format(task.getDue()))
                    .build().toString();
            log.info("updateCardOnly, URL: {}", url);
            CloseableHttpResponse createCommentResponse = httpClient.put(url, null);
            String entity = httpClient.extractEntity(createCommentResponse, true);
            return objectMapper.readTree(entity).get("id").asText(); //has 'id' => all is ok
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }

    @Deprecated
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

    @Deprecated
    String changeComment(String taskId, Comment comment, String appKey, String userToken) {
        return null;
    }

    @Deprecated
    Task parseCardInTask(String cardJson, String appKey, String userToken) throws IOException, ParseException {
        JsonNode root = objectMapper.readTree(cardJson);
        Task task = new Task();
        Auth auth = new Auth();
        auth.setTargetSystem(Auth.TargetSystem.TRELLO);
        task.setAuth(auth);
        task.setTaskId(root.get("id").asText());
        task.setContainerId(root.get("idBoard").asText());
        task.setName(root.get("name").asText());
        task.setDescription(root.get("desc").asText());
        task.setDue(TRELLO_DATE_FORMAT.parse(root.get("due").asText()));
        task.setUserId(getIdCreatorFromActions(root.get("actions"))); //from action "createCard"
        task.setAssigneeId(root.get("idMembers").get(0).asText()); //only first member is assigned for the task
        task.setRiskRef(getLinkFromAttachments(root.get("attachments")));
        //TODO: add parsing comments
        task.setStatus(getStatusByList(root.get("idList").asText(), appKey, userToken));
        return task;
    }

    @Deprecated
    private String getLinkFromAttachments(JsonNode attachmentsArray) throws IOException {
        log.info("attachments are array? {}", attachmentsArray.isArray());
        for (JsonNode attachment : attachmentsArray) {
            String name = attachment.get("name").asText();
            if (name.startsWith("http"))
                return name; //for links 'name' is URL
        }
        return null;
    }

    @Deprecated
    private String getIdCreatorFromActions(JsonNode actionsArray) {
        log.info("actions are array? {}", actionsArray.isArray());
        for (JsonNode action : actionsArray) {
            if (action.get("type").asText().equals("createCard"))
                return action.get("memberCreator").get("id").asText();
        }
        return null;
    }

    @Deprecated
    private List<Comment> getCommentsFromActions(JsonNode actionsArray) throws ParseException {
        List<Comment> comments = new ArrayList<>();
        for (JsonNode action : actionsArray) {
            if (action.get("type").asText().equals("commentCard"))
                comments.add(new CommentBuilder()
                        .setCommentId(action.get("id").asText())
                        .setDate(TRELLO_DATE_FORMAT.parse(action.get("date").asText()))
                        .setText(action.get("data").get("text").asText())
                        .build());
        }
        return comments;
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
    @Deprecated
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

    @Deprecated
    private List<Comment> getCommentsByCard(String taskId, String appKey, String userToken) throws URISyntaxException, IOException, ParseException {
        String withoutParams = MessageFormat.format(BASE_URL + GET_OR_CHANGE_CARD_BY_ID, taskId);
        String url = new URIBuilder(withoutParams)
                .addParameter("key", appKey)
                .addParameter("userToken", userToken)
                .addParameter("board_fields", "")
                .addParameter("actions", "commentCard")
                .addParameter("fields", "idBoard")
                .build().toString();
        CloseableHttpResponse response = httpClient.get(url);
        String entity = httpClient.extractEntity(response, true);
        return getCommentsFromActions(objectMapper.readTree(entity).get("actions"));
    }


}
