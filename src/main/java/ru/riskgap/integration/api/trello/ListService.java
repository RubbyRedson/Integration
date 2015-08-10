package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import ru.riskgap.integration.exceptions.InternalServerException;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by andrey on 13.07.15.
 */
public class ListService extends BaseTrelloService {
    public ListService(HttpClient httpClient) {
        super(httpClient);
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
    public String getByStatus(Task.Status status, String boardId, String appKey, String userToken) throws IOException, URISyntaxException {
        log.info("[Trello] get list id by status {} in board with id {}", "'" + status + "'", boardId);
        String withoutParams = MessageFormat.format(BASE_URL + GET_LISTS_OF_BOARD, boardId);
        String url = new URIBuilder(withoutParams)
                .addParameter("key", appKey)
                .addParameter("token", userToken)
                .build().toString();
        CloseableHttpResponse response = httpClient.get(url);
        String entity = httpClient.extractEntity(response, true);
        if (entity != null) {
            JsonNode jsonNode = objectMapper.readTree(entity);
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    String listName = node.get("name").asText();
                    if (listName.equals(STATUS_LIST_MAP.get(status)))
                        return node.get("id").asText();
                }
            }
        }
        throw new InternalServerException(MessageFormat.format("No list for status {0} in board with id {1}",
                "'"+status.getStatus()+"'", boardId));


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
    Task.Status getStatusByList(String listId, String appKey, String userToken) throws IOException, URISyntaxException {
        log.info("[Trello] get status of task by list with id: {}", listId);
        String withoutParams = MessageFormat.format(BASE_URL + GET_LIST_BY_ID, listId);
        String url = new URIBuilder(withoutParams)
                .addParameter("key", appKey)
                .addParameter("token", userToken)
                .build().toString();
        CloseableHttpResponse response = httpClient.get(url);
        String entity = httpClient.extractEntity(response, true);
        JsonNode root = objectMapper.readTree(entity);
        for (Map.Entry<Task.Status, String> entry : STATUS_LIST_MAP.entrySet()) {
            if (entry.getValue().equals(root.get("name").asText()))
                return entry.getKey();
        }
        throw new InternalServerException("Can't get status for list with id "+listId);
    }


}
