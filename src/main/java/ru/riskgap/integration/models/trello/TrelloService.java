package ru.riskgap.integration.models.trello;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.HashMap;

/**
 * Created by andrey on 06.07.15.
 */

public class TrelloService {

    private final Logger log = LoggerFactory.getLogger(TrelloService.class);

    private static final String BASE_URL = "https://api.trello.com/1/";
    private static final String GET_LISTS_OF_BOARD = "boards/{0}/lists";
    private static final HashMap<String, String> STATUS_LIST_MAP = new HashMap<String, String>() {
        {
            put("new", "To Do");
            put("finished", "Done");
        }
    };
    private HttpClient httpClient;

    public TrelloService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getListIdByStatus(String status, String boardId, String appKey, String userToken) throws IOException {
        try {
            String withoutParams = MessageFormat.format(BASE_URL + GET_LISTS_OF_BOARD, boardId);
            String url = new URIBuilder(withoutParams)
                    .addParameter("key", appKey)
                    .addParameter("token", userToken)
                    .build().toString();
            log.info("getListIdByStatus, URL: {}", url);
            CloseableHttpResponse response = httpClient.get(url);
            String entity = httpClient.extractEntity(response, true);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(entity);
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    String listName = node.get("name").asText();
                    if (STATUS_LIST_MAP.get(status).equals(listName))
                        return node.get("id").asText();
                }
            }
        } catch (URISyntaxException e) {
            log.error("Illegal Trello URL", e);
        }
        return null;
    }
}
