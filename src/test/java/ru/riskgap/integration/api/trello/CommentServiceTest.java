package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.CommentBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by andrey on 13.07.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {TestTrelloContextConfig.class})
public class CommentServiceTest {
    private static final String BOARD_ID = "myBoardId";
    private static final String CARD_ID = "myCardId";
    private static final String COMMENT_ID = "myCommentId";
    private static final String KEY = "myKeyId";
    private static final String TOKEN = "myUserToken";

    private FakeTrelloHttpClient fakeTrelloHttpClient;

    @Autowired
    private CommentService commentService;

    @PostConstruct
    public void init() {
        fakeTrelloHttpClient = (FakeTrelloHttpClient) commentService.getHttpClient();
    }

    @Test
    public void getCommentsFromActions_oneComment() throws Exception {
        String json = " [\n" +
                "        {\n" +
                "            \"id\": \"55a221dc086ba632709546eb\",\n" +
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
                "                    \"shortLink\": \"GZztVE1c\",\n" +
                "                    \"idShort\": 9,\n" +
                "                    \"name\": \"MyCard Test\",\n" +
                "                    \"id\": \"55a02f77277fb81cdaff3d33\"\n" +
                "                },\n" +
                "                \"text\": \"Test!\"\n" +
                "            },\n" +
                "            \"type\": \"commentCard\",\n" +
                "            \"date\": \"2015-07-12T08:14:20.657Z\",\n" +
                "            \"memberCreator\": {\n" +
                "                \"id\": \"5134d76e21518d64320053a7\",\n" +
                "                \"avatarHash\": \"f5d7aa54fa594f2b6ba608e870df38d9\",\n" +
                "                \"fullName\": \"Андрей Куликов\",\n" +
                "                \"initials\": \"АК\",\n" +
                "                \"username\": \"a274bae93a51409fbf7555edab1e4925\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]";
        Comment expected = new CommentBuilder()
                .setCommentId("55a221dc086ba632709546eb")
                .setUserId("5134d76e21518d64320053a7")
                .setDate(BaseTrelloService.TRELLO_DATE_FORMAT.parse("2015-07-12T08:14:20.657Z"))
                .setText("Test!").build();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actionsNode = objectMapper.readTree(json);
        Comment actual = commentService.getFromActions(actionsNode).get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void getCommentsFromActions_twoComments() throws Exception {
        String json = " [\n" +
                "        {\n" +
                "            \"id\": \"55a23241db84aedab5f34684\",\n" +
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
                "                    \"shortLink\": \"GZztVE1c\",\n" +
                "                    \"idShort\": 9,\n" +
                "                    \"name\": \"MyCard Test\",\n" +
                "                    \"id\": \"55a02f77277fb81cdaff3d33\"\n" +
                "                },\n" +
                "                \"text\": \"Another Test!\"\n" +
                "            },\n" +
                "            \"type\": \"commentCard\",\n" +
                "            \"date\": \"2015-07-12T09:24:17.017Z\",\n" +
                "            \"memberCreator\": {\n" +
                "                \"id\": \"5134d76e21518d64320053a7\",\n" +
                "                \"avatarHash\": \"f5d7aa54fa594f2b6ba608e870df38d9\",\n" +
                "                \"fullName\": \"Андрей Куликов\",\n" +
                "                \"initials\": \"АК\",\n" +
                "                \"username\": \"a274bae93a51409fbf7555edab1e4925\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"55a221dc086ba632709546eb\",\n" +
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
                "                    \"shortLink\": \"GZztVE1c\",\n" +
                "                    \"idShort\": 9,\n" +
                "                    \"name\": \"MyCard Test\",\n" +
                "                    \"id\": \"55a02f77277fb81cdaff3d33\"\n" +
                "                },\n" +
                "                \"text\": \"Test!\"\n" +
                "            },\n" +
                "            \"type\": \"commentCard\",\n" +
                "            \"date\": \"2015-07-12T08:14:20.657Z\",\n" +
                "            \"memberCreator\": {\n" +
                "                \"id\": \"5134d76e21518d64320053a7\",\n" +
                "                \"avatarHash\": \"f5d7aa54fa594f2b6ba608e870df38d9\",\n" +
                "                \"fullName\": \"Андрей Куликов\",\n" +
                "                \"initials\": \"АК\",\n" +
                "                \"username\": \"a274bae93a51409fbf7555edab1e4925\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]";
        List<Comment> expected = Arrays.asList(
                new CommentBuilder()
                        .setCommentId("55a23241db84aedab5f34684")
                        .setUserId("5134d76e21518d64320053a7")
                        .setDate(BaseTrelloService.TRELLO_DATE_FORMAT.parse("2015-07-12T09:24:17.017Z"))
                        .setText("Another Test!").build(),
                new CommentBuilder()
                        .setCommentId("55a221dc086ba632709546eb")
                        .setUserId("5134d76e21518d64320053a7")
                        .setDate(BaseTrelloService.TRELLO_DATE_FORMAT.parse("2015-07-12T08:14:20.657Z"))
                        .setText("Test!").build());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actionsNode = objectMapper.readTree(json);
        List<Comment> actual = commentService.getFromActions(actionsNode);
        assertEquals(expected, actual);
    }

    @Test
    public void create_smokeTest() {
        String json = "{\n" +
                "    \"id\": \"COMMENT_ID\",\n" +
                "    \"idMemberCreator\": \"5134d76e21518d64320053a7\",\n" +
                "    \"data\": {\n" +
                "        \"text\": \"Hello!\",\n" +
                "        \"textData\": {\n" +
                "            \"emoji\": {}\n" +
                "        },\n" +
                "        \"card\": {\n" +
                "            \"id\": \"CARD_ID\",\n" +
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
                "}";
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.POST_OR_DElETE_COMMENT, json);
        Comment comment = new CommentBuilder().setText("Hello!").build();
        Exception ex = null;
        try {
            commentService.create("CARD_ID", comment, KEY, TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        } finally {
            assertNull(ex);
        }
        assertEquals("COMMENT_ID", comment.getCommentId());
    }

    @Test
    public void update_smokeTest() {
        String json = "{\n" +
                "    \"id\": \"COMMENT_ID\",\n" +
                "    \"idMemberCreator\": \"5134d76e21518d64320053a7\",\n" +
                "    \"data\": {\n" +
                "        \"text\": \"Hello!\",\n" +
                "        \"textData\": {\n" +
                "            \"emoji\": {}\n" +
                "        },\n" +
                "        \"card\": {\n" +
                "            \"id\": \"CARD_ID\",\n" +
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
                "}";
        Comment comment = new CommentBuilder().setCommentId(COMMENT_ID).setText("My wonderful world!").build();
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.PUT_COMMENT, json);
        Exception ex = null;
        try {
            commentService.update(CARD_ID, comment, KEY, TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        } finally {
            assertNull(ex);
        }
    }

    @Test
    public void delete_smokeTest() {
        Comment comment = new CommentBuilder().setText("Comment!").setCommentId(COMMENT_ID).build();
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.POST_OR_DElETE_COMMENT, "{}");
        Exception ex = null;
        try {
            commentService.delete(CARD_ID, comment, KEY, TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        } finally {
            assertNull(ex);
        }
    }

    @Test
    public void sync_onlyNewComments() {
        fakeTrelloHttpClient.resetCounters();
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.POST_OR_DElETE_COMMENT,
                "{\n" +
                        "    \"id\": \"COMMENT_ID\",\n" +
                        "    \"idMemberCreator\": \"5134d76e21518d64320053a7\",\n" +
                        "    \"data\": {\n" +
                        "        \"text\": \"Hello!\",\n" +
                        "        \"textData\": {\n" +
                        "            \"emoji\": {}\n" +
                        "        },\n" +
                        "        \"card\": {\n" +
                        "            \"id\": \"CARD_ID\",\n" +
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
                        "}"
        );
        List<Comment> newComments = Arrays.asList(
                new CommentBuilder()
                        .setUserId("userId1")
                        .setText("Hello1")
                        .setDate(new Date()).build(),
                new CommentBuilder()
                        .setUserId("userId2")
                        .setText("Hello2")
                        .setDate(new Date()).build(),
                new CommentBuilder()
                        .setUserId("userId3")
                        .setText("Hello3")
                        .setDate(new Date()).build()
        );
        Exception ex = null;
        try {
            commentService.sync(CARD_ID, newComments, new ArrayList<Comment>(), KEY, TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        } finally {
            assertNull(ex);
            assertEquals(3, fakeTrelloHttpClient.getPostCounter());
        }
    }

    @Test
    public void sync_deleteAllComments() throws IOException {
        fakeTrelloHttpClient.resetCounters();
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.POST_OR_DElETE_COMMENT, "{}");
        List<Comment> currentComments = Arrays.asList(
                new CommentBuilder().setCommentId(COMMENT_ID).setText("Hello1!").build(),
                new CommentBuilder().setCommentId(COMMENT_ID + "2").setText("Hello2!").build(),
                new CommentBuilder().setCommentId(COMMENT_ID + "3").setText("Hello3!").build()
        );
        Exception ex = null;
        try {
            commentService.sync(CARD_ID, new ArrayList<Comment>(), currentComments, KEY, TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        } finally {
            assertNull(ex);
            assertEquals(3, fakeTrelloHttpClient.getDeleteCounter());
        }
    }

    @Test
    public void sync_updateAll() {
        fakeTrelloHttpClient.resetCounters();
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.PUT_COMMENT,
                "{\n" +
                        "    \"id\": \"COMMENT_ID\",\n" +
                        "    \"idMemberCreator\": \"5134d76e21518d64320053a7\",\n" +
                        "    \"data\": {\n" +
                        "        \"text\": \"Hello!\",\n" +
                        "        \"textData\": {\n" +
                        "            \"emoji\": {}\n" +
                        "        },\n" +
                        "        \"card\": {\n" +
                        "            \"id\": \"CARD_ID\",\n" +
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
                        "}"
        );
        List<Comment> currentComments = Arrays.asList(
                new CommentBuilder().setCommentId(COMMENT_ID).setText("Hello1!").build(),
                new CommentBuilder().setCommentId(COMMENT_ID + "2").setText("Hello2!").build(),
                new CommentBuilder().setCommentId(COMMENT_ID + "3").setText("Hello3!").build()
        );
        List<Comment> newComments = Arrays.asList(
                new CommentBuilder().setCommentId(COMMENT_ID).setText("Hello1!").build(),
                new CommentBuilder().setCommentId(COMMENT_ID + "2").setText("Hello2!").build(),
                new CommentBuilder().setCommentId(COMMENT_ID + "3").setText("Hello3!").build()
        );
        Exception ex = null;
        try {
            commentService.sync(CARD_ID, newComments, currentComments, KEY, TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        } finally {
            assertNull(ex);
            assertEquals(3, fakeTrelloHttpClient.getPutCounter());
        }
    }

    @Test
    public void sync_createUpdate() {
        String json =
                "{\n" +
                        "    \"id\": \"COMMENT_ID\",\n" +
                        "    \"idMemberCreator\": \"5134d76e21518d64320053a7\",\n" +
                        "    \"data\": {\n" +
                        "        \"text\": \"Hello!\",\n" +
                        "        \"textData\": {\n" +
                        "            \"emoji\": {}\n" +
                        "        },\n" +
                        "        \"card\": {\n" +
                        "            \"id\": \"CARD_ID\",\n" +
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
                        "}";
        fakeTrelloHttpClient.resetCounters();
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.POST_OR_DElETE_COMMENT, json);
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.PUT_COMMENT, json);
        List<Comment> newComments = Arrays.asList(
                new CommentBuilder().setCommentId(COMMENT_ID).setText("Message 1").build(),
                new CommentBuilder().setCommentId(COMMENT_ID + "1").setText("Message 2").build(),
                new CommentBuilder().setText("Message 3").build()
        );
        List<Comment> currentComments = Arrays.asList(
                new CommentBuilder().setCommentId(COMMENT_ID).setText("Message #1").build(),
                new CommentBuilder().setCommentId(COMMENT_ID + "1").setText("Message #2").build()
        );
        Exception ex = null;
        try {
            commentService.sync(CARD_ID, newComments, currentComments, KEY, TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        } finally {
            assertNull(ex);
            assertEquals(2, fakeTrelloHttpClient.getPutCounter());
            assertEquals(1, fakeTrelloHttpClient.getPostCounter());
        }

    }

    @Test
    public void sync_createUpdateDelete() {
        fakeTrelloHttpClient.resetCounters();
        String json =
                "{\n" +
                        "    \"id\": \"COMMENT_ID\",\n" +
                        "    \"idMemberCreator\": \"5134d76e21518d64320053a7\",\n" +
                        "    \"data\": {\n" +
                        "        \"text\": \"Hello!\",\n" +
                        "        \"textData\": {\n" +
                        "            \"emoji\": {}\n" +
                        "        },\n" +
                        "        \"card\": {\n" +
                        "            \"id\": \"CARD_ID\",\n" +
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
                        "}";
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.PUT_COMMENT, json);
        fakeTrelloHttpClient.setEntityForResponse(FakeTrelloHttpClient.POST_OR_DElETE_COMMENT, json);
        List<Comment> newComments = Arrays.asList(
                new CommentBuilder().setCommentId(COMMENT_ID).setText("Message 1").build(),
                new CommentBuilder().setText("Message 3").build()
        );
        List<Comment> currentComments = Arrays.asList(
                new CommentBuilder().setCommentId(COMMENT_ID).setText("Changed Message 1").build(),
                new CommentBuilder().setCommentId(COMMENT_ID+"1").setText("Will be deleted").build()
        );
        Exception ex = null;
        try {
            commentService.sync(CARD_ID, newComments, currentComments, KEY, TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        } finally {
            assertNull(ex);
            assertEquals(1, fakeTrelloHttpClient.getPutCounter());
            assertEquals(1, fakeTrelloHttpClient.getPostCounter());
            assertEquals(1, fakeTrelloHttpClient.getDeleteCounter());
        }




    }
}
