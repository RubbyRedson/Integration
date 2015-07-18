package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.riskgap.integration.models.Auth;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrey on 13.07.15.
 */
@Component
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
    public Task getById(String cardId, String appKey, String userToken) throws IOException, ParseException {
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
            return fromJson(entity, appKey, userToken);
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }

    public Task save(Task task, String appKey, String userToken) throws IOException {
        if (task.getTaskId() == null)
            return create(task, appKey, userToken);
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

    Task update(Task task, String appKey, String userToken) throws IOException, URISyntaxException, ParseException {
        task.setTaskId(updateWithoutComments(task, appKey, userToken)); //id doesn't change
        List<Comment> newComments = new ArrayList<>();
        //TODO: move functional to 'syncComments' in CommentService
        newComments.addAll(task.getComments()); //make copy
        List<Comment> currentComments = commentSrvc.getByCard(task.getTaskId(), appKey, userToken);
        for (Comment newComment : newComments) {
            if (newComment.getCommentId() == null) {
                commentSrvc.create(task.getTaskId(), newComment, appKey, userToken);
            } else {
                Comment currentComment = commentSrvc.findById(newComment.getCommentId(), currentComments);

            }
        }
        //under construction
        return null;
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

    String updateWithoutComments(Task task, String appKey, String userToken) throws IOException {
        String withoutParams = MessageFormat.format(BASE_URL + GET_OR_CHANGE_CARD_BY_ID, task.getTaskId());
        //TODO: can link to a risk be changed?
        try {
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
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }

    Task fromJson(String cardJson, String appKey, String userToken) throws IOException, ParseException {
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
        task.setStatus(listSrvc.getStatusByList(root.get("idList").asText(), appKey, userToken));
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
