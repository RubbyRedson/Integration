package ru.riskgap.integration.api.trello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.riskgap.integration.exceptions.InvalidInputDataException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;
import static ru.riskgap.integration.api.trello.FakeTrelloHttpClient.FIND_USER_BY_ID;

/**
 * Created by andrey on 25.07.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {TestTrelloContextConfig.class})
public class UserServiceTest {
    private static final String KEY = "myKeyId";
    private static final String TOKEN = "myUserToken";

    private FakeTrelloHttpClient fakeTrelloHttpClient;


    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        fakeTrelloHttpClient = (FakeTrelloHttpClient) userService.getHttpClient();
    }

    @Test
    public void findByEmail_oneCandidate() {
        fakeTrelloHttpClient.setEntityForResponse(FIND_USER_BY_ID,
                "[\n" +
                        "  {\n" +
                        "    \"id\": \"5134d76e21518d64320053a7\",\n" +
                        "    \"username\": \"a274bae93a51409fbf7555edab1e4925\",\n" +
                        "    \"avatarHash\": \"f5d7aa54fa594f2b6ba608e870df38d9\",\n" +
                        "    \"initials\": \"АК\",\n" +
                        "    \"fullName\": \"Андрей Куликов\",\n" +
                        "    \"email\": null,\n" +
                        "    \"idBoards\": [\n" +
                        "      \"5134d76e21518d64320053a9\",\n" +
                        "      \"513a12d752b9afe929004632\",\n" +
                        "      \"5543660180fbd72623622de0\",\n" +
                        "      \"554371eb6b3505a16d9177c6\",\n" +
                        "      \"559381ce9af4e9c91ab2dbad\",\n" +
                        "      \"55a2100bb31ac682f59aff01\"\n" +
                        "    ],\n" +
                        "    \"idOrganizations\": [\n" +
                        "      \"5593b0584457f37d08b6b6f8\"\n" +
                        "    ],\n" +
                        "    \"confirmed\": true,\n" +
                        "    \"memberType\": \"normal\",\n" +
                        "    \"similarity\": 7,\n" +
                        "    \"active\": true\n" +
                        "  }\n" +
                        "]");
        Exception ex = null;
        try {
            String id = userService.findIdByEmail("a@a.ru", KEY, TOKEN);
            assertEquals("5134d76e21518d64320053a7", id);
        } catch (URISyntaxException | IOException e) {
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void findByEmail_twoCandidates() {
        fakeTrelloHttpClient.setEntityForResponse(FIND_USER_BY_ID,
            "[\n" +
                    "  {\n" +
                    "    \"id\": \"5134d76e21518d64320053a7\",\n" +
                    "    \"username\": \"a274bae93a51409fbf7555edab1e4925\",\n" +
                    "    \"avatarHash\": \"f5d7aa54fa594f2b6ba608e870df38d9\",\n" +
                    "    \"initials\": \"АК\",\n" +
                    "    \"fullName\": \"Андрей Куликов\",\n" +
                    "    \"email\": null,\n" +
                    "    \"idBoards\": [\n" +
                    "      \"5134d76e21518d64320053a9\",\n" +
                    "      \"513a12d752b9afe929004632\",\n" +
                    "      \"5543660180fbd72623622de0\",\n" +
                    "      \"554371eb6b3505a16d9177c6\",\n" +
                    "      \"559381ce9af4e9c91ab2dbad\",\n" +
                    "      \"55a2100bb31ac682f59aff01\"\n" +
                    "    ],\n" +
                    "    \"idOrganizations\": [\n" +
                    "      \"5593b0584457f37d08b6b6f8\"\n" +
                    "    ],\n" +
                    "    \"confirmed\": true,\n" +
                    "    \"similarity\": 7,\n" +
                    "    \"active\": true\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": \"55b23f0708e2a8b8a597c9e1\",\n" +
                    "    \"username\": \"user16379995\",\n" +
                    "    \"avatarHash\": \"7c7f45b89da5ba5e8b3f8f5c02c31871\",\n" +
                    "    \"initials\": \"АК\",\n" +
                    "    \"fullName\": \"Андрей Куликов\",\n" +
                    "    \"email\": null,\n" +
                    "    \"idBoards\": [],\n" +
                    "    \"idOrganizations\": [],\n" +
                    "    \"confirmed\": true,\n" +
                    "    \"similarity\": 0,\n" +
                    "    \"active\": true\n" +
                    "  }\n"+
                    "]");
        Exception ex = null;
        try {
            String id = userService.findIdByEmail("a@a.ru", KEY, TOKEN);
            assertEquals("5134d76e21518d64320053a7", id);
        } catch (URISyntaxException | IOException e) {
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void findByEmail_noCandidates() {
        fakeTrelloHttpClient.setEntityForResponse(FIND_USER_BY_ID,
                "[\n" +
                        "  {\n" +
                        "    \"email\": \"a@a.ru\",\n" +
                        "    \"similarity\": 0,\n" +
                        "    \"active\": false\n" +
                        "  }\n" +
                        "]");
        Exception ex = null;
        InvalidInputDataException inputDataException = null;
        try {
            String id = userService.findIdByEmail("a@a.ru", KEY, TOKEN);
        } catch (URISyntaxException | IOException e) {
            ex = e;
        } catch (InvalidInputDataException invEx) {
            inputDataException = invEx;
        }
        assertNull(ex);
        assertNotNull(inputDataException);
    }

    @Test
    public void findByEmail_noCandidatesAlt() {
        fakeTrelloHttpClient.setEntityForResponse(FIND_USER_BY_ID, "[]");
        Exception ex = null;
        InvalidInputDataException inputDataException = null;
        try {
            String id = userService.findIdByEmail("a@a.ru", KEY, TOKEN);
        } catch (URISyntaxException | IOException e) {
            ex = e;
        } catch (InvalidInputDataException invEx) {
            inputDataException = invEx;
        }
        assertNull(ex);
        assertNotNull(inputDataException);
    }

}