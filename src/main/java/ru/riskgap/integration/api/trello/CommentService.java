package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
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

    public List<Comment> getByCard(String cardId, String appKey, String userToken) throws URISyntaxException, IOException, ParseException {
        log.info("[Trello] get comments of card with id {}", cardId);
        String withoutParams = MessageFormat.format(BASE_URL + GET_OR_CHANGE_CARD_BY_ID, cardId);
        String url = new URIBuilder(withoutParams)
                .addParameter("key", appKey)
                .addParameter("token", userToken)
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
                        .setDate(parseDate(action.get("date").asText()))
                        .setText(action.get("data").get("text").asText())
                        .setUserId(action.get("memberCreator").get("id").asText())
                        .build());
        }
        return comments;
    }


    Comment create(String cardId, Comment comment, String appKey, String userToken) throws IOException, URISyntaxException {
        String withoutParams = MessageFormat.format(BASE_URL + POST_COMMENT, cardId);
        String url = new URIBuilder(withoutParams)
                .addParameter("key", appKey)
                .addParameter("token", userToken)
                .addParameter("text", comment.getText())
                .build().toString();
        log.info("[Trello] create comment: {}", comment);
        CloseableHttpResponse createCommentResponse = httpClient.post(url, null);
        String entity = httpClient.extractEntity(createCommentResponse, true);
        comment.setCommentId(objectMapper.readTree(entity).get("id").asText());
        return comment;
    }

    Comment update(String cardId, Comment comment, String appKey, String userToken) throws IOException {
        String withoutParams = MessageFormat.format(BASE_URL + CHANGE_OR_DELETE_COMMENT, cardId, comment.getCommentId());
        try {
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .addParameter("text", comment.getText())
                    .build().toString();
            log.info("[Trello] update comment: {}", comment);
            CloseableHttpResponse updateCommentResponse = httpClient.put(url, null);
            String entity = httpClient.extractEntity(updateCommentResponse, true);
            comment.setCommentId(objectMapper.readTree(entity).get("id").asText());
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return comment;
    }

    void delete(String cardId, Comment comment, String appKey, String userToken) throws IOException, URISyntaxException {
        String withoutParams = MessageFormat.format(BASE_URL + CHANGE_OR_DELETE_COMMENT, cardId, comment.getCommentId());
        String url = new URIBuilder(withoutParams)
                .addParameter("key", appKey)
                .addParameter("token", userToken)
                .build().toString();
        log.info("[Trello] delete comment: {}", comment);
        CloseableHttpResponse updateCommentResponse = httpClient.delete(url);
        String entity = httpClient.extractEntity(updateCommentResponse, true);
    }

    /**
     * Synchronizes new and current lists of comments for the given card. Supported operations:
     * create, update, delete. Returns list of new comments with filled IDs.
     *
     * @param cardId          ID of card in Trello
     * @param newComments     new list of comments for cardId
     * @param currentComments current list comments for cardId
     * @param appKey          application key
     * @param userToken       token of the user, who has access to the card
     * @return list of new comments with filled IDs
     * @throws IOException
     */
    List<Comment> sync(String cardId, List<Comment> newComments, List<Comment> currentComments, String appKey, String userToken) throws IOException, URISyntaxException {
        log.info("[Trello] sync comments: START");
        List<Comment> currCommentsCopy = new ArrayList<>(currentComments.size());
        currCommentsCopy.addAll(currentComments);
        if (newComments != null) {
            for (Comment newComment : newComments) {
                if (newComment.getCommentId() == null) {
                    create(cardId, newComment, appKey, userToken);
                    continue;
                }
                Comment currComment = findById(newComment.getCommentId(), currentComments);
                if (currComment == null) {
                    throw new IllegalStateException("Comment with id=" + newComment.getCommentId() + " doesn't exist");
                }
                //newComment - updated version of existing comment
                update(cardId, newComment, appKey, userToken);
                currCommentsCopy.remove(currComment);
            }
        }
        for (Comment remaining : currCommentsCopy) {
            delete(cardId, remaining, appKey, userToken);
        }
        log.info("[Trello] sync comments: END");
        return newComments;
    }
}
