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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by andrey on 13.07.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {TestTrelloContextConfig.class})
public class CommentServiceTest {
    private static final String BOARD_ID = "myBoardId";
    private static final String CARD_ID = "myCardId";
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
                        .setDate(BaseTrelloService.TRELLO_DATE_FORMAT.parse("2015-07-12T09:24:17.017Z"))
                        .setText("Another Test!").build(),
                new CommentBuilder()
                        .setCommentId("55a221dc086ba632709546eb")
                        .setDate(BaseTrelloService.TRELLO_DATE_FORMAT.parse("2015-07-12T08:14:20.657Z"))
                        .setText("Test!").build());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actionsNode = objectMapper.readTree(json);
        List<Comment> actual = commentService.getFromActions(actionsNode);
        assertEquals(expected, actual);

    }
}
