package ru.riskgap.integration.api.trello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.riskgap.integration.models.Task;

import javax.annotation.PostConstruct;
import java.io.IOException;

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

    private FakeTrelloHttpClient fakeTrelloHttpClient;

    @Autowired
    private ListService listService;

    @PostConstruct
    public void init() {
        fakeTrelloHttpClient = (FakeTrelloHttpClient) listService.getHttpClient();
    }

    @Test
    public void getByStatus_testRequestUrl() throws IOException {
        listService.getByStatus(Task.Status.OPEN, BOARD_ID, KEY, TOKEN);
        assertEquals("https://api.trello.com/1/boards/" + BOARD_ID + "/lists?key=" + KEY + "&token=" + TOKEN,
                fakeTrelloHttpClient.getLastUrl());
    }

    @Test
    public void getByStatus_taskOpenStatus() throws IOException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.GET_ALL_LISTS_OF_BOARD,
                "[\n" +
                        "    {\n" +
                        "        \"id\": \"559381ce9af4e9c91ab2dbae\",\n" +
                        "        \"name\": \"To Do\",\n" +
                        "        \"closed\": false,\n" +
                        "        \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "        \"pos\": 16384,\n" +
                        "        \"subscribed\": false\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"id\": \"559381cf9af4e9c91ab2dbaf\",\n" +
                        "        \"name\": \"Doing\",\n" +
                        "        \"closed\": false,\n" +
                        "        \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "        \"pos\": 32768,\n" +
                        "        \"subscribed\": false\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"id\": \"559381cf9af4e9c91ab2dbb0\",\n" +
                        "        \"name\": \"Done\",\n" +
                        "        \"closed\": false,\n" +
                        "        \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "        \"pos\": 49152,\n" +
                        "        \"subscribed\": false\n" +
                        "    }\n" +
                        "]");
        String toDoTask = listService.getByStatus(Task.Status.OPEN, BOARD_ID, KEY, TOKEN);
        assertEquals("559381ce9af4e9c91ab2dbae", toDoTask);
    }

    @Test
    public void getByStatus_closedStatus() throws IOException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.GET_ALL_LISTS_OF_BOARD,
                "[\n" +
                        "    {\n" +
                        "        \"id\": \"559381ce9af4e9c91ab2dbae\",\n" +
                        "        \"name\": \"To Do\",\n" +
                        "        \"closed\": false,\n" +
                        "        \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "        \"pos\": 16384,\n" +
                        "        \"subscribed\": false\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"id\": \"559381cf9af4e9c91ab2dbaf\",\n" +
                        "        \"name\": \"Doing\",\n" +
                        "        \"closed\": false,\n" +
                        "        \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "        \"pos\": 32768,\n" +
                        "        \"subscribed\": false\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"id\": \"559381cf9af4e9c91ab2dbb0\",\n" +
                        "        \"name\": \"Done\",\n" +
                        "        \"closed\": false,\n" +
                        "        \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "        \"pos\": 49152,\n" +
                        "        \"subscribed\": false\n" +
                        "    }\n" +
                        "]");
        String doneTask = listService.getByStatus(Task.Status.CLOSED, BOARD_ID, KEY, TOKEN);
        assertEquals("559381cf9af4e9c91ab2dbb0", doneTask);
    }

    @Test
    public void getStatusByList_openStatus() throws IOException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.GET_LIST_BY_ID,
                "{\n" +
                        "    \"id\": \"559381ce9af4e9c91ab2dbae\",\n" +
                        "    \"name\": \"To Do\",\n" +
                        "    \"closed\": false,\n" +
                        "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "    \"pos\": 16384\n" +
                        "}");
        Task.Status status = listService.getStatusByList("559381ce9af4e9c91ab2dbae", KEY, TOKEN);
        assertEquals(Task.Status.OPEN, status);
    }

    @Test
    public void getStatusByList_closedStatus() throws IOException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.GET_LIST_BY_ID,
                "{\n" +
                        "    \"id\": \"559381cf9af4e9c91ab2dbb0\",\n" +
                        "    \"name\": \"Done\",\n" +
                        "    \"closed\": false,\n" +
                        "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "    \"pos\": 49152\n" +
                        "}");
        Task.Status status = listService.getStatusByList("559381cf9af4e9c91ab2dbb0", KEY, TOKEN);
        assertEquals(Task.Status.CLOSED, status);
    }

}