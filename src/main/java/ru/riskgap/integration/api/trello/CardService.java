package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.models.TaskBuilder;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;

/**
 * Created by andrey on 13.07.15.
 */
public class CardService extends BaseTrelloService {

    @Autowired
    CommentService commentSrvc;
    @Autowired
    ListService listSrvc;

    public CardService(HttpClient httpClient) {
        super(httpClient);
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
    public Task getById(String cardId, String appKey, String userToken) throws IOException, ParseException, URISyntaxException {
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
            return fromJson(entity, appKey, userToken);
        }

    public Task save(Task task, String appKey, String userToken) throws IOException, ParseException, URISyntaxException {
        if (task.getTaskId() == null)
            return create(task, appKey, userToken);
//        return update(task, appKey, userToken);
        return update(task, appKey, userToken);
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
    Task create(Task task, String appKey, String userToken) throws IOException {
        task.setTaskId(createWithoutComments(task, appKey, userToken));
        List<Comment> comments = task.getComments();
        if (comments != null) {
            for (Comment comment : comments) {
                comment.setCommentId(commentSrvc
                        .create(task.getTaskId(), comment, appKey, userToken)
                        .getCommentId());
            }
        }
        return task;
    }

    Task update(Task task, String appKey, String userToken) throws IOException, ParseException, URISyntaxException {
        task.setTaskId(updateWithoutComments(task, appKey, userToken)); //id doesn't change
        List<Comment> newComments = task.getComments();
        List<Comment> currentComments = commentSrvc.getByCard(task.getTaskId(),
                task.getAuth().getApplicationKey(), task.getAuth().getUserToken());
        commentSrvc.sync(task.getTaskId(), newComments, currentComments,
                task.getAuth().getApplicationKey(), task.getAuth().getUserToken());
        return task;
    }

    String createWithoutComments(Task task, String appKey, String userToken) throws IOException {
        String withoutParams = BASE_URL + POST_CARD;
        try {
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .addParameter("name", task.getName())
                    .addParameter("desc", task.getDescription())
                    .addParameter("idList",
                            listSrvc.getByStatus(task.getStatus(), task.getContainerId(), appKey, userToken))
                    .addParameter("due", TRELLO_DATE_FORMAT.format(task.getDue()))
                    .addParameter("urlSource", task.getRiskRef())
                    .build().toString();
            log.info("createWithoutComments, URL: {}", url);
            CloseableHttpResponse createCardResponse = httpClient.post(url, null);
            String entity = httpClient.extractEntity(createCardResponse, true);
            return objectMapper.readTree(entity).get("id").asText();
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }

    String updateWithoutComments(Task task, String appKey, String userToken) throws IOException, URISyntaxException {
        String withoutParams = MessageFormat.format(BASE_URL + GET_OR_CHANGE_CARD_BY_ID, task.getTaskId());
        //TODO: can link to a risk be changed?
        String url = new URIBuilder(withoutParams)
                .addParameter("key", appKey)
                .addParameter("token", userToken)
                .addParameter("name", task.getName())
                .addParameter("desc", task.getDescription())
                .addParameter("idList",
                        listSrvc.getByStatus(task.getStatus(), task.getContainerId(), appKey, userToken))
                .addParameter("due", TRELLO_DATE_FORMAT.format(task.getDue()))
                .build().toString();
        log.info("updateWithoutComments, URL: {}", url);
        CloseableHttpResponse createCommentResponse = httpClient.put(url, null);
        String entity = httpClient.extractEntity(createCommentResponse, true);
        return objectMapper.readTree(entity).get("id").asText(); //has 'id' => all is ok
    }

    Task fromJson(String cardJson, String appKey, String userToken) throws IOException, ParseException {
        JsonNode root = objectMapper.readTree(cardJson);
        Task task = new TaskBuilder()
                .setTaskId(root.get("id").asText())
                .setContainerId(root.get("idBoard").asText())
                .setName(root.get("name").asText())
                .setDescription(root.get("desc").asText())
                .setDue(TRELLO_DATE_FORMAT.parse(root.get("due").asText()))
                .setUserId(getIdCreatorFromActions(root.get("actions"))) //from action "createCard"
                .setAssigneeId(root.get("idMembers").get(0).asText()) //only first member is assigned for the task
                .setRiskRef(getLinkFromAttachments(root.get("attachments")))
                .setStatus(listSrvc.getStatusByList(root.get("idList").asText(), appKey, userToken))
                .setComments(commentSrvc.getFromActions(root.get("actions"))).build();
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
}
