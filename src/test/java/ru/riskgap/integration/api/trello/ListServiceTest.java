package ru.riskgap.integration.api.trello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.riskgap.integration.models.Task;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

/**
 * Created by andrey on 13.07.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {TestTrelloContextConfig.class})
public class ListServiceTest {
    private static final String BOARD_ID = "myBoardId";
    private static final String CARD_ID = "myCardId";
    private static final String KEY = "myKeyId";
    private static final String TOKEN = "myUserToken";

    private static final String ALL_LISTS = "[\n" +
            "    {\n" +
            "        \"id\": \"TODO\",\n" +
            "        \"name\": \"To Do\",\n" +
            "        \"closed\": false,\n" +
            "        \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
            "        \"pos\": 16384,\n" +
            "        \"subscribed\": false\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"DOING\",\n" +
            "        \"name\": \"Doing\",\n" +
            "        \"closed\": false,\n" +
            "        \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
            "        \"pos\": 32768,\n" +
            "        \"subscribed\": false\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"RESOLVED\",\n" +
            "        \"name\": \"Done\",\n" +
            "        \"closed\": false,\n" +
            "        \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
            "        \"pos\": 49152,\n" +
            "        \"subscribed\": false\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"CLOSED\",\n" +
            "        \"name\": \"Closed\",\n" +
            "        \"closed\": false,\n" +
            "        \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
            "        \"pos\": 49152,\n" +
            "        \"subscribed\": false\n" +
            "    }\n" +
            "]";

    private FakeTrelloHttpClient fakeTrelloHttpClient;

    @Autowired
    private ListService listService;

    @PostConstruct
    public void init() {
        fakeTrelloHttpClient = (FakeTrelloHttpClient) listService.getHttpClient();
    }

    @Test
    public void getByStatus_testRequestUrl() throws IOException, URISyntaxException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.GET_ALL_LISTS_OF_BOARD, ALL_LISTS);
        listService.getByStatus(Task.Status.OPEN, BOARD_ID, KEY, TOKEN);
        assertEquals("https://api.trello.com/1/boards/" + BOARD_ID + "/lists?key=" + KEY + "&token=" + TOKEN,
                fakeTrelloHttpClient.getLastUrl());
    }

    @Test
    public void getByStatus_taskOpenStatus() throws IOException, URISyntaxException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.GET_ALL_LISTS_OF_BOARD, ALL_LISTS);
        String toDoTask = listService.getByStatus(Task.Status.OPEN, BOARD_ID, KEY, TOKEN);
        assertEquals("TODO", toDoTask);
    }

    @Test
    public void getByStatus_closedStatus() throws IOException, URISyntaxException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.GET_ALL_LISTS_OF_BOARD,
                ALL_LISTS);
        String doneTask = listService.getByStatus(Task.Status.CLOSED, BOARD_ID, KEY, TOKEN);
        assertEquals("CLOSED", doneTask);
    }

    @Test
    public void getStatusByList_openStatus() throws IOException, URISyntaxException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.GET_LIST_BY_ID,
                "{\n" +
                        "    \"id\": \"OPEN\",\n" +
                        "    \"name\": \"To Do\",\n" +
                        "    \"closed\": false,\n" +
                        "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "    \"pos\": 16384\n" +
                        "}");
        Task.Status status = listService.getStatusByList("OPEN", KEY, TOKEN);
        assertEquals(Task.Status.OPEN, status);
    }

    @Test
    public void getStatusByList_closedStatus() throws IOException, URISyntaxException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.GET_LIST_BY_ID,
                "{\n" +
                        "    \"id\": \"CLOSED\",\n" +
                        "    \"name\": \"Closed\",\n" +
                        "    \"closed\": false,\n" +
                        "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "    \"pos\": 49152\n" +
                        "}");
        Task.Status status = listService.getStatusByList("CLOSED", KEY, TOKEN);
        assertEquals(Task.Status.CLOSED, status);
    }

}