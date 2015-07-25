package ru.riskgap.integration.api.trello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.riskgap.integration.models.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static ru.riskgap.integration.api.trello.CardServiceContextConfig.OPEN_LIST;

/**
 * Created by andrey on 13.07.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {CardServiceContextConfig.class})
public class CardServiceTest {
    private static final String BOARD_ID = "myBoardId";
    private static final String CARD_ID = "myCardId";
    private static final String KEY = "myKeyId";
    private static final String TOKEN = "myUserToken";

    private FakeTrelloHttpClient fakeTrelloHttpClient;

    @Autowired
    private CardService cardService;

    @PostConstruct
    public void init() {
        fakeTrelloHttpClient = (FakeTrelloHttpClient) cardService.getHttpClient();
    }


    @Test(expected = NullPointerException.class) //test for URL only, not for real content
    public void getTaskByCardId_testRequestUrl() throws Exception {
        try {
            cardService.getById(CARD_ID, KEY, TOKEN);
        } finally {
            assertEquals("https://api.trello.com/1/cards/" + CARD_ID + "?key=" + KEY + "&token=" + TOKEN +
                            "&attachments=true&actions=createCard%2CcommentCard",
                    fakeTrelloHttpClient.getLastUrl());
        }
    }

    @Test
    public void parseCardInTask_openStatus() throws Exception {
        String jsonCard = "{\n" +
                "    \"id\": \"559a048632b6165b1416dabd\",\n" +
                "    \"badges\": {\n" +
                "        \"votes\": 0,\n" +
                "        \"viewingMemberVoted\": false,\n" +
                "        \"subscribed\": true,\n" +
                "        \"fogbugz\": \"\",\n" +
                "        \"checkItems\": 0,\n" +
                "        \"checkItemsChecked\": 0,\n" +
                "        \"comments\": 1,\n" +
                "        \"attachments\": 2,\n" +
                "        \"description\": true,\n" +
                "        \"due\": \"2015-05-07T04:00:00.000Z\"\n" +
                "    },\n" +
                "    \"checkItemStates\": [],\n" +
                "    \"closed\": false,\n" +
                "    \"dateLastActivity\": \"2015-07-10T14:05:39.535Z\",\n" +
                "    \"desc\": \"Test Description!\",\n" +
                "    \"descData\": {\n" +
                "        \"emoji\": {}\n" +
                "    },\n" +
                "    \"due\": \"2015-05-07T04:00:00.000Z\",\n" +
                "    \"email\": \"a274bae93a51409fbf7555edab1e4925+5134d76e21518d64320053a7+559a048632b6165b1416dabd+3ebad028f337ad72f586ea2fc5329af7da2a802a@boards.trello.com\",\n" +
                "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                "    \"idChecklists\": [],\n" +
                "    \"idLabels\": [],\n" +
                "    \"idList\": \""+ OPEN_LIST +"\",\n" +
                "    \"idMembers\": [\n" +
                "        \"5134d76e21518d64320053a7\"\n" +
                "    ],\n" +
                "    \"idShort\": 3,\n" +
                "    \"idAttachmentCover\": \"\",\n" +
                "    \"manualCoverAttachment\": false,\n" +
                "    \"labels\": [],\n" +
                "    \"name\": \"Simple task\",\n" +
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
                "        },\n" +
                "        {\n" +
                "            \"id\": \"559fd13359c4a2e8153c4153\",\n" +
                "            \"bytes\": null,\n" +
                "            \"date\": \"2015-07-10T14:05:39.526Z\",\n" +
                "            \"edgeColor\": null,\n" +
                "            \"idMember\": \"5134d76e21518d64320053a7\",\n" +
                "            \"isUpload\": false,\n" +
                "            \"mimeType\": \"\",\n" +
                "            \"name\": \"http://4pda.ru\",\n" +
                "            \"previews\": [],\n" +
                "            \"url\": \"http://4pda.ru\"\n" +
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
        Task actual = cardService.fromJson(jsonCard, KEY, TOKEN);

        Task expected = new TaskBuilder()
                .setUserId("5134d76e21518d64320053a7")
                .setAssigneeId("5134d76e21518d64320053a7")
                .setContainerId("559381ce9af4e9c91ab2dbad")
                .setTaskId("559a048632b6165b1416dabd")
                .setName("Simple task")
                .setDescription("Test Description!")
                .setStatus(Task.Status.OPEN)
                .setDue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2015-05-07T04:00:00.000Z"))
                .setRiskRef("http://google.ru")
                .setComments(new ArrayList<Comment>())
                .build();
        assertEquals(expected, actual);
    }


    @Test
    public void create_noComments() throws ParseException, IOException, URISyntaxException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.POST_CARD,
                "{\n" +
                        "    \"id\": \"55a02f77277fb81cdaff3d33\",\n" +
                        "    \"badges\": {\n" +
                        "        \"votes\": 0,\n" +
                        "        \"viewingMemberVoted\": false,\n" +
                        "        \"subscribed\": false,\n" +
                        "        \"fogbugz\": \"\",\n" +
                        "        \"checkItems\": 0,\n" +
                        "        \"checkItemsChecked\": 0,\n" +
                        "        \"comments\": 0,\n" +
                        "        \"attachments\": 1,\n" +
                        "        \"description\": false,\n" +
                        "        \"due\": \"2015-05-07T04:00:00.000Z\"\n" +
                        "    },\n" +
                        "    \"checkItemStates\": [],\n" +
                        "    \"closed\": false,\n" +
                        "    \"dateLastActivity\": \"2015-07-10T20:47:51.998Z\",\n" +
                        "    \"desc\": \"\",\n" +
                        "    \"descData\": {\n" +
                        "        \"emoji\": {}\n" +
                        "    },\n" +
                        "    \"due\": \"2015-05-07T04:00:00.000Z\",\n" +
                        "    \"email\": \"a274bae93a51409fbf7555edab1e4925+5134d76e21518d64320053a7+55a02f77277fb81cdaff3d33+8f93b1222f3bd9c099c5709cede336ecc4c44714@boards.trello.com\",\n" +
                        "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "    \"idChecklists\": [],\n" +
                        "    \"idLabels\": [],\n" +
                        "    \"idList\": \"" + OPEN_LIST + "\",\n" +
                        "    \"idMembers\": [],\n" +
                        "    \"idShort\": 9,\n" +
                        "    \"idAttachmentCover\": null,\n" +
                        "    \"manualCoverAttachment\": false,\n" +
                        "    \"labels\": [],\n" +
                        "    \"name\": \"MyCard\",\n" +
                        "    \"pos\": 163839,\n" +
                        "    \"shortUrl\": \"https://trello.com/c/GZztVE1c\",\n" +
                        "    \"url\": \"https://trello.com/c/GZztVE1c/9-mycard\",\n" +
                        "    \"stickers\": []\n" +
                        "}");
        Task task = new TaskBuilder()
                .setContainerId("559381ce9af4e9c91ab2dbae")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("05.07.2015"))
                .setRiskRef("http://google.ru")
                .setName("MyCard")
                .setStatus(Task.Status.OPEN)
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.TRELLO)
                        .setApplicationKey(KEY)
                        .setUserToken(TOKEN)
                        .build())
                .build();
        cardService.create(task, task.getAuth().getApplicationKey(), task.getAuth().getUserToken());
        assertEquals("55a02f77277fb81cdaff3d33", task.getTaskId());
    }




}