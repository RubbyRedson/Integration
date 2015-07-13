package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.CommentBuilder;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by andrey on 13.07.15.
 */
@Component
public class CommentService extends BaseTrelloService {

    public CommentService(HttpClient httpClient) {
        super(httpClient);
    }

    Comment findById(String commentId, Collection<Comment> comments) {
        for (Comment comment : comments) {
            if (commentId.equals(comment.getCommentId()))
                return comment;
        }
        return null;
    }

    public List<Comment> getByCard(String taskId, String appKey, String userToken) throws URISyntaxException, IOException, ParseException {
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
        return getFromActions(objectMapper.readTree(entity).get("actions"));
    }


    List<Comment> getFromActions(JsonNode actionsArray) throws ParseException {
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


    String create(String taskId, Comment comment, String appKey, String userToken) throws IOException {
        String withoutParams = MessageFormat.format(BASE_URL + POST_COMMENT, taskId);
        try {
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .addParameter("text", comment.getText())
                    .build().toString();
            log.info("create, URL: {}", url);
            CloseableHttpResponse createCommentResponse = httpClient.post(url, null);
            String entity = httpClient.extractEntity(createCommentResponse, true);
            return objectMapper.readTree(entity).get("id").asText();
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }

    String update(String taskId, Comment comment, String appKey, String userToken) {
        return null;
    }

    void delete(String taskId, Comment comment, String appKey, String userToken) {

    }

    void syncComments(List<Comment> newComments, List<Comment> currentCommnents) {

    }


}
