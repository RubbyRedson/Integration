package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import ru.riskgap.integration.exceptions.InvalidInputDataException;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;

import static ru.riskgap.integration.exceptions.InvalidInputDataException.Reason.INCORRECT;

/**
 * Created by andrey on 25.07.15.
 */

public class UserService extends BaseTrelloService {
    public UserService(HttpClient httpClient) {
        super(httpClient);
    }

    public String findIdByEmail(String email, String appKey, String userToken) throws URISyntaxException, IOException {
        String withoutParams = BASE_URL + FIND_USER_BY_EMAIL;
        String url = new URIBuilder(withoutParams)
                .addParameter("key", appKey)
                .addParameter("token", userToken)
                .addParameter("query", email)
                .build().toString();
        log.info("[Trello] find user by mail: {}", email);
        CloseableHttpResponse userByEmail = httpClient.get(url);
        String entity = httpClient.extractEntity(userByEmail, true);
        JsonNode root = objectMapper.readTree(entity);
        if (root.get(0) == null) {
            throw new InvalidInputDataException("userEmail", INCORRECT);
        }
        JsonNode userId = root.get(0).get("id");
        if (userId == null)
            throw new InvalidInputDataException("userEmail", INCORRECT);
        return userId.asText();
    }
}
