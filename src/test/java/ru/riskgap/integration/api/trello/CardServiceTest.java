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
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by andrey on 13.07.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {TestTrelloContextConfig.class})
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
    public void getTaskByCardId_testRequestUrl() throws IOException, ParseException, URISyntaxException {
        try {
            cardService.getById(CARD_ID, KEY, TOKEN);
        } finally {
            assertEquals("https://api.trello.com/1/cards/" + CARD_ID + "?key=" + KEY + "&token=" + TOKEN +
                            "&attachments=true&actions=createCard%2CcommentCard",
                    fakeTrelloHttpClient.getLastUrl());
        }
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
                "    \"idList\": \"559381ce9af4e9c91ab2dbae\",\n" +
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
                "            \"id\": \"559f59621ff3af6b3a01cc02\",\n" +
                "            \"idMemberCreator\": \"5134d76e21518d64320053a7\",\n" +
                "            \"data\": {\n" +
                "                \"list\": {\n" +
                "                    \"name\": \"To Do\",\n" +
                "                    \"id\": \"559381ce9af4e9c91ab2dbae\"\n" +
                "                },\n" +
                "                \"board\": {\n" +
                "                    \"shortLink\": \"3RNfcaVO\",\n" +
                "                    \"name\": \"Test Integrations!\",\n" +
                "                    \"id\": \"559381ce9af4e9c91ab2dbad\"\n" +
                "                },\n" +
                "                \"card\": {\n" +
                "                    \"shortLink\": \"2ToaglTp\",\n" +
                "                    \"idShort\": 3,\n" +
                "                    \"name\": \"Simple task\",\n" +
                "                    \"id\": \"559a048632b6165b1416dabd\"\n" +
                "                },\n" +
                "                \"text\": \"dfdf\"\n" +
                "            },\n" +
                "            \"type\": \"commentCard\",\n" +
                "            \"date\": \"2015-07-10T05:34:26.863Z\",\n" +
                "            \"memberCreator\": {\n" +
                "                \"id\": \"5134d76e21518d64320053a7\",\n" +
                "                \"avatarHash\": \"f5d7aa54fa594f2b6ba608e870df38d9\",\n" +
                "                \"fullName\": \"Андрей Куликов\",\n" +
                "                \"initials\": \"АК\",\n" +
                "                \"username\": \"a274bae93a51409fbf7555edab1e4925\"\n" +
                "            }\n" +
                "        },\n" +
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
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.GET_LIST_BY_ID,
                "{\n" +
                        "    \"id\": \"559381ce9af4e9c91ab2dbae\",\n" +
                        "    \"name\": \"To Do\",\n" +
                        "    \"closed\": false,\n" +
                        "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "    \"pos\": 16384\n" +
                        "}");
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
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.TRELLO)
                        .build())
                .build();
        assertEquals(expected, actual);
    }


    @Test
    public void create_noComments() throws ParseException, IOException {
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
                        "    \"idList\": \"559381ce9af4e9c91ab2dbae\",\n" +
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
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.TRELLO)
                        .setApplicationKey(KEY)
                        .setUserToken(TOKEN)
                        .build())
                .build();
        cardService.create(task, task.getAuth().getApplicationKey(), task.getAuth().getUserToken());
        assertEquals("55a02f77277fb81cdaff3d33", task.getTaskId());
    }

    @Test
    //checks for filling ids
    public void create_withComments() throws ParseException, IOException {
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.POST_CARD,
                "{\n" +
                        "    \"id\": \"55a02f77277fb81cdaff3d33\",\n" +
                        "    \"checkItemStates\": [],\n" +
                        "    \"closed\": false,\n" +
                        "    \"desc\": \"\",\n" +
                        "    \"descData\": {\n" +
                        "        \"emoji\": {}\n" +
                        "    },\n" +
                        "    \"due\": \"2015-05-07T04:00:00.000Z\",\n" +
                        "    \"idBoard\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "    \"idChecklists\": [],\n" +
                        "    \"idLabels\": [],\n" +
                        "    \"idList\": \"559381ce9af4e9c91ab2dbae\",\n" +
                        "    \"idMembers\": [],\n" +
                        "    \"name\": \"MyCard\",\n" +
                        "    \"pos\": 163839,\n" +
                        "    \"shortUrl\": \"https://trello.com/c/GZztVE1c\",\n" +
                        "    \"url\": \"https://trello.com/c/GZztVE1c/9-mycard\",\n" +
                        "    \"stickers\": []\n" +
                        "}");
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.POST_OR_DElETE_COMMENT,
                "{\n" +
                        "    \"id\": \"55a033df0c69600cdadfd912\",\n" +
                        "    \"idMemberCreator\": \"5134d76e21518d64320053a7\",\n" +
                        "    \"data\": {\n" +
                        "        \"text\": \"Hello!\",\n" +
                        "        \"textData\": {\n" +
                        "            \"emoji\": {}\n" +
                        "        },\n" +
                        "        \"card\": {\n" +
                        "            \"id\": \"559a0a3d41fbf1920844aade\",\n" +
                        "            \"name\": \"MyCard\",\n" +
                        "            \"idShort\": 4,\n" +
                        "            \"shortLink\": \"JAT9xeHO\"\n" +
                        "        },\n" +
                        "        \"board\": {\n" +
                        "            \"id\": \"559381ce9af4e9c91ab2dbad\",\n" +
                        "            \"name\": \"Test Integrations!\",\n" +
                        "            \"shortLink\": \"3RNfcaVO\"\n" +
                        "        },\n" +
                        "        \"list\": {\n" +
                        "            \"id\": \"559381ce9af4e9c91ab2dbae\",\n" +
                        "            \"name\": \"To Do\"\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"type\": \"commentCard\",\n" +
                        "    \"date\": \"2015-07-10T21:06:39.904Z\",\n" +
                        "    \"entities\": [\n" +
                        "        {\n" +
                        "            \"type\": \"member\",\n" +
                        "            \"id\": \"5134d76e21518d64320053a7\",\n" +
                        "            \"username\": \"a274bae93a51409fbf7555edab1e4925\",\n" +
                        "            \"text\": \"Андрей Куликов\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"type\": \"text\",\n" +
                        "            \"text\": \"on\",\n" +
                        "            \"hideIfContext\": true,\n" +
                        "            \"idContext\": \"559a0a3d41fbf1920844aade\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"type\": \"card\",\n" +
                        "            \"hideIfContext\": true,\n" +
                        "            \"shortLink\": \"JAT9xeHO\",\n" +
                        "            \"id\": \"559a0a3d41fbf1920844aade\",\n" +
                        "            \"text\": \"MyCard\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"type\": \"comment\",\n" +
                        "            \"text\": \"Hello!\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}");
        Task task = new TaskBuilder()
                .setContainerId("559381ce9af4e9c91ab2dbae")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("05.07.2015"))
                .setRiskRef("http://google.ru")
                .setName("MyCard")
                .setComments(Arrays.asList(
                        new Comment(new Date(), "Hello!"),
                        new Comment(new Date(), "Good Bye!")))
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.TRELLO)
                        .setApplicationKey(KEY)
                        .setUserToken(TOKEN)
                        .build())
                .build();
        cardService.create(task, task.getAuth().getApplicationKey(), task.getAuth().getUserToken());
        assertEquals("55a02f77277fb81cdaff3d33", task.getTaskId());
        assertEquals(2, task.getComments().size());
        assertEquals("55a033df0c69600cdadfd912", task.getComments().get(0).getCommentId());
        assertEquals("55a033df0c69600cdadfd912", task.getComments().get(1).getCommentId());
    }


}