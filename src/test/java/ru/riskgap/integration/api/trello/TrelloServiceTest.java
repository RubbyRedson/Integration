package ru.riskgap.integration.api.trello;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.riskgap.integration.models.Task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

/**
 * Created by andrey on 06.07.15.
 */
public class TrelloServiceTest {

    private static TrelloService trelloService;
    private static FakeTrelloHttpClient fakeTrelloHttpClient;

    private static final String BOARD_ID = "myBoardId";
    private static final String KEY = "myKeyId";
    private static final String TOKEN = "myUserToken";

    @BeforeClass
    public static void initTest() {
        fakeTrelloHttpClient = new FakeTrelloHttpClient();
        trelloService = new TrelloService(fakeTrelloHttpClient);
    }


    @Test
    public void getListIdByStatus_testRequestUrl() throws IOException {
        trelloService.getListIdByStatus(Task.Status.OPEN, BOARD_ID, KEY, TOKEN);
        assertEquals("https://api.trello.com/1/boards/" + BOARD_ID + "/lists?key=" + KEY + "&token=" + TOKEN,
                fakeTrelloHttpClient.getLastUrl());

    }

    @Test
    public void getListIdByStatus_taskOpenStatus() throws IOException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.ALL_LISTS_OF_BOARD,
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
        String toDoTask = trelloService.getListIdByStatus(Task.Status.OPEN, BOARD_ID, KEY, TOKEN);
        assertEquals("559381ce9af4e9c91ab2dbae", toDoTask);
    }


    @Test
    public void getListIdByStatus_closedStatus() throws IOException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.ALL_LISTS_OF_BOARD,
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
        String doneTask = trelloService.getListIdByStatus(Task.Status.CLOSED, BOARD_ID, KEY, TOKEN);
        assertEquals("559381cf9af4e9c91ab2dbb0", doneTask);
    }

    @Test
    public void getStatusByList_openStatus() throws IOException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.LIST_BY_ID,
                "{\n" +
                        "    \"id\": \"559381ce9af4e9c91ab2dbae\",\n" +
                        "    \"name\": \"To Do\",\n" +
                        "    \"closed\": false,\n" +
                        "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "    \"pos\": 16384\n" +
                        "}");
        Task.Status status = trelloService.getStatusByList("559381ce9af4e9c91ab2dbae", KEY, TOKEN);
        assertEquals(Task.Status.OPEN, status);
    }

    @Test
    public void getStatusByList_closedStatus() throws IOException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.LIST_BY_ID,
                "{\n" +
                        "    \"id\": \"559381cf9af4e9c91ab2dbb0\",\n" +
                        "    \"name\": \"Done\",\n" +
                        "    \"closed\": false,\n" +
                        "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "    \"pos\": 49152\n" +
                        "}");
        Task.Status status = trelloService.getStatusByList("559381cf9af4e9c91ab2dbb0", KEY, TOKEN);
        assertEquals(Task.Status.CLOSED, status);
    }

    @Test
    public void parseCardInTask_openStatus() throws IOException, ParseException {
        String jsonCard = "{\n" +
                "    \"id\": \"559a048632b6165b1416dabd\",\n" +
                "    \"badges\": {\n" +
                "        \"votes\": 0,\n" +
                "        \"viewingMemberVoted\": false,\n" +
                "        \"subscribed\": true,\n" +
                "        \"fogbugz\": \"\",\n" +
                "        \"checkItems\": 0,\n" +
                "        \"checkItemsChecked\": 0,\n" +
                "        \"comments\": 0,\n" +
                "        \"attachments\": 1,\n" +
                "        \"description\": true,\n" +
                "        \"due\": \"2015-05-07T04:00:00.000Z\"\n" +
                "    },\n" +
                "    \"checkItemStates\": [],\n" +
                "    \"closed\": false,\n" +
                "    \"dateLastActivity\": \"2015-07-08T08:19:26.821Z\",\n" +
                "    \"desc\": \"Test Description!\",\n" +
                "    \"descData\": {\n" +
                "        \"emoji\": {}\n" +
                "    },\n" +
                "    \"due\": \"2015-05-07T04:00:00.000Z\",\n" +
                "    \"email\": \"a274bae93a51409fbf7555edab1e4925+5134d76e21518d64320053a7+559a048632b6165b1416dabd+3ebad028f337ad72f586ea2fc5329af7da2a802a@boards.trello.com\",\n" +
                "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                "    \"idChecklists\": [],\n" +
                "    \"idLabels\": [],\n" +
                "    \"idList\": \"559381ce9af4e9c91ab2dbae\",\n" +
                "    \"idMembers\": [\n" +
                "        \"5134d76e21518d64320053a7\"\n" +
                "    ],\n" +
                "    \"idShort\": 3,\n" +
                "    \"idAttachmentCover\": \"\",\n" +
                "    \"manualCoverAttachment\": false,\n" +
                "    \"labels\": [],\n" +
                "    \"name\": \"Risk Gap task\",\n" +
                "    \"pos\": 81919,\n" +
                "    \"shortUrl\": \"https://trello.com/c/2ToaglTp\",\n" +
                "    \"url\": \"https://trello.com/c/2ToaglTp/3-risk-gap-task\",\n" +
                "    \"attachments\": [\n" +
                "        {\n" +
                "            \"id\": \"559cc88fbbc38d10aa1c3dc0\",\n" +
                "            \"bytes\": null,\n" +
                "            \"date\": \"2015-07-08T06:51:59.607Z\",\n" +
                "            \"edgeColor\": null,\n" +
                "            \"idMember\": \"5134d76e21518d64320053a7\",\n" +
                "            \"isUpload\": false,\n" +
                "            \"mimeType\": \"\",\n" +
                "            \"name\": \"http://google.ru\",\n" +
                "            \"previews\": [],\n" +
                "            \"url\": \"http://google.ru\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"members\": [\n" +
                "        {\n" +
                "            \"id\": \"5134d76e21518d64320053a7\",\n" +
                "            \"avatarHash\": \"f5d7aa54fa594f2b6ba608e870df38d9\",\n" +
                "            \"fullName\": \"Андрей Куликов\",\n" +
                "            \"initials\": \"АК\",\n" +
                "            \"username\": \"a274bae93a51409fbf7555edab1e4925\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"actions\": [\n" +
                "        {\n" +
                "            \"id\": \"559a048632b6165b1416dac0\",\n" +
                "            \"idMemberCreator\": \"5134d76e21518d64320053a7\",\n" +
                "            \"data\": {\n" +
                "                \"board\": {\n" +
                "                    \"shortLink\": \"3RNfcaVO\",\n" +
                "                    \"name\": \"Test Integrations!\",\n" +
                "                    \"id\": \"559381ce9af4e9c91ab2dbad\"\n" +
                "                },\n" +
                "                \"list\": {\n" +
                "                    \"name\": \"To Do\",\n" +
                "                    \"id\": \"559381ce9af4e9c91ab2dbae\"\n" +
                "                },\n" +
                "                \"card\": {\n" +
                "                    \"shortLink\": \"2ToaglTp\",\n" +
                "                    \"idShort\": 3,\n" +
                "                    \"name\": \"http://google.ru\",\n" +
                "                    \"id\": \"559a048632b6165b1416dabd\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"type\": \"createCard\",\n" +
                "            \"date\": \"2015-07-06T04:31:02.308Z\",\n" +
                "            \"memberCreator\": {\n" +
                "                \"id\": \"5134d76e21518d64320053a7\",\n" +
                "                \"avatarHash\": \"f5d7aa54fa594f2b6ba608e870df38d9\",\n" +
                "                \"fullName\": \"Андрей Куликов\",\n" +
                "                \"initials\": \"АК\",\n" +
                "                \"username\": \"a274bae93a51409fbf7555edab1e4925\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.LIST_BY_ID,
                "{\n" +
                        "    \"id\": \"559381ce9af4e9c91ab2dbae\",\n" +
                        "    \"name\": \"To Do\",\n" +
                        "    \"closed\": false,\n" +
                        "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "    \"pos\": 16384\n" +
                        "}");
        Task actual = trelloService.parseCardInTask(jsonCard, KEY, TOKEN);
        Task expected = new Task();
        expected.setUserId("5134d76e21518d64320053a7");
        expected.setAssigneeId("5134d76e21518d64320053a7");
        expected.setContainerId("559381ce9af4e9c91ab2dbad");
        expected.setTaskId("559a048632b6165b1416dabd");
        expected.setName("Risk Gap task");
        expected.setDescription("Test Description!");
        expected.setStatus(Task.Status.OPEN);
        expected.setDue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2015-05-07T04:00:00.000Z"));
        expected.setRiskRef("http://google.ru");
        expected.setTargetSystem(Task.TargetSystem.TRELLO);
        assertEquals(expected, actual);
        System.out.println(actual);

    }


}