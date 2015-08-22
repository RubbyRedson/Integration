package ru.riskgap.integration.api.tfs;

import org.junit.Test;
import ru.riskgap.integration.models.Auth;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.Task;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Nikita on 05.07.2015.
 */
public class TfsResponseParseTest {
    //unit test, no spring autowiring
    TfsResponseParser parser = new TfsResponseParser();


    private void test(String input, Task expected) throws IOException {
        Task actual = parser.parseTfsGetWorkItemFieldsResponseJson(input);
        assertEquals("TFS JSON parsing test failed", expected, actual);
    }

    @Test
    public void testOneRequest() {
        String json = "{\n" +
                "   \"count\": 1,\n" +
                "   \"value\": [   {\n" +
                "      \"id\": 2,\n" +
                "      \"rev\": 2,\n" +
                "      \"fields\":       {\n" +
                "         \"System.State\": \"open\",\n" +
                "         \"System.AssignedTo\": \"rg <RISKGAPWIN\\\\rg>\",\n" +
                "         \"System.ChangedBy\": \"rg <RISKGAPWIN\\\\rg>\",\n" +
                "         \"System.Title\": \"Test Task\",\n" +
                "         \"System.Description\": \"Test descr\"\n" +
                "      },\n" +
                "      \"url\": \"http://riskgapwin:8080/tfs/NIGU%20Test%20Collection/_apis/wit/workItems/2\"\n" +
                "   }]\n" +
                "}";

        Task expected = new Task();
        expected.setName("Test Task");
        expected.setStatus(Task.Status.OPEN);
        expected.setDescription("Test descr");
        expected.setUsername("rg");
        expected.setUserEmail("RISKGAPWIN\\rg");
        expected.setAssigneeUsername("rg");
        expected.setAssigneeEmail("RISKGAPWIN\\rg");
//		try {
//			expected.setDue(Task.DATE_FORMATTER.get().parse("12.02.2015"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		expected.setRiskRef("test risk reference");
        Auth auth = new Auth();
        auth.setTargetSystem(Auth.TargetSystem.TFS);
        expected.setAuth(auth);


        try {
            test(json, expected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSeveralRequestsSequentially() {
        String json1 = "{\n" +
                "   \"count\": 1,\n" +
                "   \"value\": [   {\n" +
                "      \"id\": 2,\n" +
                "      \"rev\": 2,\n" +
                "      \"fields\":       {\n" +
                "         \"System.State\": \"open\",\n" +
                "         \"System.AssignedTo\": \"rg <RISKGAPWIN\\\\rg>\",\n" +
                "         \"System.ChangedBy\": \"rg <RISKGAPWIN\\\\rg>\",\n" +
                "         \"System.Title\": \"Test Task\",\n" +
                "         \"System.Description\": \"Test descr\"\n" +
                "      },\n" +
                "      \"url\": \"http://riskgapwin:8080/tfs/NIGU%20Test%20Collection/_apis/wit/workItems/2\"\n" +
                "   }]\n" +
                "}";

        Task expected1 = new Task();
        expected1.setName("Test Task");
        expected1.setStatus(Task.Status.OPEN);
        expected1.setDescription("Test descr");
        expected1.setUsername("rg");
        expected1.setUserEmail("RISKGAPWIN\\rg");
        expected1.setAssigneeUsername("rg");
        expected1.setAssigneeEmail("RISKGAPWIN\\rg");
//		try {
//			expected1.setDue(Task.DATE_FORMATTER.get().parse("12.02.2015"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		expected1.setRiskRef("test risk reference");
        Auth auth = new Auth();
        auth.setTargetSystem(Auth.TargetSystem.TFS);
        expected1.setAuth(auth);

        String json2 = "{\n" +
                "   \"count\": 1,\n" +
                "   \"value\": [   {\n" +
                "      \"id\": 3,\n" +
                "      \"rev\": 4,\n" +
                "      \"fields\":       {\n" +
                "         \"System.State\": \"open\",\n" +
                "         \"System.AssignedTo\": \"rg <RISKGAPWIN\\\\rg>\",\n" +
                "         \"System.ChangedBy\": \"rg <RISKGAPWIN\\\\rg>\",\n" +
                "         \"System.Title\": \"Test Task 2\",\n" +
                "         \"System.Description\": \"Test descr 2\"\n" +
                "      },\n" +
                "      \"url\": \"http://riskgapwin:8080/tfs/NIGU%20Test%20Collection/_apis/wit/workItems/2\"\n" +
                "   }]\n" +
                "}";

        Task expected2 = new Task();
        expected2.setName("Test Task 2");
        expected2.setStatus(Task.Status.OPEN);
        expected2.setDescription("Test descr 2");
        expected2.setUsername("rg");
        expected2.setUserEmail("RISKGAPWIN\\rg");
        expected2.setAssigneeUsername("rg");
        expected2.setAssigneeEmail("RISKGAPWIN\\rg");
//		try {
//			expected2.setDue(Task.DATE_FORMATTER.get().parse("14.02.2015"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		expected2.setRiskRef("test risk reference 2");
        Auth auth2 = new Auth();
        auth2.setTargetSystem(Auth.TargetSystem.TFS);
        expected2.setAuth(auth2);

        try {
            test(json1, expected1);
            test(json2, expected2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPartialTaskRequest() {
        String json = "{\n" +
                "   \"count\": 1,\n" +
                "   \"value\": [   {\n" +
                "      \"id\": 2,\n" +
                "      \"rev\": 2,\n" +
                "      \"fields\":       {\n" +
                "         \"System.State\": \"open\",\n" +
                "         \"System.Title\": \"Test Task\",\n" +
                "         \"System.Description\": \"Test descr\"\n" +
                "      },\n" +
                "      \"url\": \"http://riskgapwin:8080/tfs/NIGU%20Test%20Collection/_apis/wit/workItems/2\"\n" +
                "   }]\n" +
                "}";

        Task expected = new Task();
        expected.setName("Test Task");
        expected.setStatus(Task.Status.OPEN);
        expected.setDescription("Test descr");
//		try {
//			expected.setDue(Task.DATE_FORMATTER.get().parse("12.02.2015"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		expected.setRiskRef("test risk reference");
        Auth auth = new Auth();
        auth.setTargetSystem(Auth.TargetSystem.TFS);
        expected.setAuth(auth);

        try {
            test(json, expected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmptyRequest() {
        String json = "{\n" +
                "}";

        Task expected = new Task();

        try {
            test(json, expected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMalformedJson() throws IOException {
        String json = "I don't look like a JSON\n" +
                "Do I?" +
                "See ya!";

        IOException expected = new IOException("Unrecognized token 'I': was expecting 'null', 'true', 'false' or NaN\n" +
                " at [Source: I don't look like a JSON\n" +
                "Do I?See ya!; line: 1, column: 2]");
        Exception actual = null;

        try {
            parser.parseTfsGetWorkItemFieldsResponseJson(json);
        } catch (IOException e) {
            actual = e;
            assertEquals("Expected IOException with message, but received", expected.getMessage(), actual.getMessage());
        }
        assertNotNull("Expected IOException but none was thrown", actual);
    }

    @Test
    public void testParseCommentsResponse() throws ParseException {
        String response = "{\n" +
                "\t\"count\": 3,\n" +
                "\t\"value\":\r [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\"rev\": 2,\n" +
                "\t\t\t\t\"value\": \"Adding the necessary spec\",\n" +
                "\t\t\t\t\"revisedBy\":          {\n" +
                "\t\t\t\t\t\"id\": \"e05ad0af-18c6-46eb-ac02-bab333d46f5c\",\n" +
                "\t\t\t\t\t\"name\": \"rg <RISKGAPWIN\\\\rg>\",\n" +
                "\t\t\t\t\t\"url\": \"http://riskgapwin:8080/tfs/NIGU Test Collection/_apis/Identities/e05ad0af-18c6-46eb-ac02-bab333d46f5c\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"revisedDate\": \"2015-07-07T09:08:49.31Z\",\n" +
                "\t\t\t\t\"url\": \"http://riskgapwin:8080/tfs/NIGU%20Test%20Collection/_apis/wit/workItems/2/history/2\"\n" +
                "\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\"rev\": 2,\n" +
                "\t\t\t\t\"value\": \"Adding the necessary spec\",\n" +
                "\t\t\t\t\"revisedBy\":          {\n" +
                "\t\t\t\t\t\"id\": \"e05ad0af-18c6-46eb-ac02-bab333d46f5c\",\n" +
                "\t\t\t\t\t\"name\": \"rg <RISKGAPWIN\\\\rg>\",\n" +
                "\t\t\t\t\t\"url\": \"http://riskgapwin:8080/tfs/NIGU Test Collection/_apis/Identities/e05ad0af-18c6-46eb-ac02-bab333d46f5c\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"revisedDate\": \"2015-07-07T09:08:49.31Z\",\n" +
                "\t\t\t\t\"url\": \"http://riskgapwin:8080/tfs/NIGU%20Test%20Collection/_apis/wit/workItems/2/history/2\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";
        List<Comment> actual = new ArrayList<>();
        try {
            actual = parser.parseTfsGetWorkItemHistoryResponseJson(response);
        } catch (IOException e) {
            assertNull("Test failed, expected no exception", e);
        }

        Comment comment1 = new Comment();
        comment1.setUsername("rg");
        comment1.setEmail("RISKGAPWIN\\rg");
        comment1.setText("Adding the necessary spec");
        comment1.setDate(TfsResponseParser.TFS_DATE_FORMATTER.get().parse("2015-07-07T09:08:49.31Z"));

        assertTrue("The length of resulting list should be 2", actual.size() == 2);
        assertEquals("Comment is not correct", comment1, actual.get(0));
        assertEquals("Comment is not correct", comment1, actual.get(1));
    }

    // todo malformed username/assignee names tests, Due Date?
}
