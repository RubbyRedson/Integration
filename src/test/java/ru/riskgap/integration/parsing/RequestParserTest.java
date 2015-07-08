package ru.riskgap.integration.parsing;

import org.junit.Test;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.CustomJsonDateDeserializer;
import ru.riskgap.integration.models.TargetSystemEnum;
import ru.riskgap.integration.models.Task;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for parser
 * Created by Nikita on 03.07.2015.
 */

public class RequestParserTest {
    //unit test, no spring autowiring
    RequestParser requestParser = new RequestParser();

    private void testInputJson(String input, Task expected) throws IOException, ParseException {
        Task actual = requestParser.parse(input);
        assertEquals("JSON parsing test", expected, actual);
    }

    @Test
    public void testInputOneRequest() {
        Exception ex = null;
        String json = "{\n" +
                "  \"container-id\": \"Project X\",\n" +
                "  \"task-id\": \"12\",\n" +
                "  \"name\": \"Test Task One\",\n" +
                "  \"status\": \"open\",\n" +
                "  \"description\": \"Test Task Description\",\n" +
                "  \"user-id\": \"42\",\n" +
                "  \"username\": \"Test user\",\n" +
                "  \"user-email\": \"testuser@riskgap.ru\",\n" +
                "  \"assignee-id\": \"34\",\n" +
                "  \"assignee-username\": \"Test assignee\",\n" +
                "  \"assignee-email\": \"testassignee@riskgap.ru\",\n" +
                "  \"due\": \"12.02.2015\",\n" +
                "  \"risk-reference\": \"test risk reference\",\n" +
                "  \"target-system\": \"TFS\"\n" +
                "}";

        Task expected = new Task();
        expected.setContainerId("Project X");
        expected.setTaskId("12");
        expected.setName("Test Task One");
        expected.setStatus(Task.Status.OPEN);
        expected.setDescription("Test Task Description");
        expected.setUserId("42");
        expected.setUsername("Test user");
        expected.setUserEmail("testuser@riskgap.ru");
        expected.setAssigneeId("34");
        expected.setAssigneeUsername("Test assignee");
        expected.setAssigneeEmail("testassignee@riskgap.ru");
        try {
            expected.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expected.setRiskRef("test risk reference");
        expected.setTargetSystem(TargetSystemEnum.TFS);

        try {
            testInputJson(json, expected);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testInputSeveralRequestsSequentially() {
        Exception ex = null;
        String json1 = "{\n" +
                "  \"name\": \"Test Task One\",\n" +
                "  \"status\": \"open\",\n" +
                "  \"description\": \"Test Task Description\",\n" +
                "  \"user-id\": \"42\",\n" +
                "  \"username\": \"Test user\",\n" +
                "  \"user-email\": \"testuser@riskgap.ru\",\n" +
                "  \"assignee-id\": \"34\",\n" +
                "  \"assignee-username\": \"Test assignee\",\n" +
                "  \"assignee-email\": \"testassignee@riskgap.ru\",\n" +
                "  \"due\": \"12.02.2015\",\n" +
                "  \"risk-reference\": \"test risk reference\",\n" +
                "  \"target-system\": \"MS_PROJECT\"\n" +
                "}";

        Task expected1 = new Task();
        expected1.setName("Test Task One");
        expected1.setStatus(Task.Status.OPEN);
        expected1.setDescription("Test Task Description");
        expected1.setUserId("42");
        expected1.setUsername("Test user");
        expected1.setUserEmail("testuser@riskgap.ru");
        expected1.setAssigneeId("34");
        expected1.setAssigneeUsername("Test assignee");
        expected1.setAssigneeEmail("testassignee@riskgap.ru");
        try {
            expected1.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expected1.setRiskRef("test risk reference");
        expected1.setTargetSystem(TargetSystemEnum.MS_PROJECT);

        String json2 = "{\n" +
                "  \"name\": \"Test Task Two\",\n" +
                "  \"status\": \"closed\",\n" +
                "  \"description\": \"Test Task2 Description\",\n" +
                "  \"user-id\": \"423\",\n" +
                "  \"username\": \"Test user 2\",\n" +
                "  \"user-email\": \"testuser2@riskgap.ru\",\n" +
                "  \"assignee-id\": \"42\",\n" +
                "  \"assignee-username\": \"Test assignee 2\",\n" +
                "  \"assignee-email\": \"testassignee2@riskgap.ru\",\n" +
                "  \"due\": \"14.02.2015\",\n" +
                "  \"risk-reference\": \"test risk reference 2\",\n" +
                "  \"target-system\": \"TFS\"\n" +
                "}";

        Task expected2 = new Task();
        expected2.setName("Test Task Two");
        expected2.setStatus(Task.Status.CLOSED);
        expected2.setDescription("Test Task2 Description");
        expected2.setUserId("423");
        expected2.setUsername("Test user 2");
        expected2.setUserEmail("testuser2@riskgap.ru");
        expected2.setAssigneeId("42");
        expected2.setAssigneeUsername("Test assignee 2");
        expected2.setAssigneeEmail("testassignee2@riskgap.ru");
        try {
            expected2.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("14.02.2015"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expected2.setRiskRef("test risk reference 2");
        expected2.setTargetSystem(TargetSystemEnum.TFS);

        try {
            testInputJson(json1, expected1);
            testInputJson(json2, expected2);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testInputPartialTaskRequest() {
        Exception ex = null;
        String json = "{\n" +
                "  \"name\": \"Test Task One\",\n" +
                "  \"status\": \"closed\",\n" +
                "  \"assignee-id\": \"34\",\n" +
                "  \"assignee-username\": \"Test assignee\",\n" +
                "  \"assignee-email\": \"testassignee@riskgap.ru\",\n" +
                "  \"due\": \"12.02.2015\",\n" +
                "  \"risk-reference\": \"test risk reference\",\n" +
                "  \"target-system\": \"TFS\"\n" +
                "}";

        Task expected = new Task();
        expected.setName("Test Task One");
        expected.setStatus(Task.Status.CLOSED);
        expected.setAssigneeId("34");
        expected.setAssigneeUsername("Test assignee");
        expected.setAssigneeEmail("testassignee@riskgap.ru");
        try {
            expected.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expected.setRiskRef("test risk reference");
        expected.setTargetSystem(TargetSystemEnum.TFS);

        try {
            testInputJson(json, expected);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testInputEmptyRequest() {
        Exception ex = null;
        String json = "{\n" +
                "}";

        Task expected = new Task();

        try {
            testInputJson(json, expected);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testInputMalformedJson() throws IOException {
        String json = "I don't look like a JSON\n" +
                "Do I?" +
                "See ya!";

        IOException expected = new IOException("Unrecognized token 'I': was expecting 'null', 'true', 'false' or NaN\n" +
                " at [Source: I don't look like a JSON\n" +
                "Do I?See ya!; line: 1, column: 2]");
        Exception actual = null;

        try {
            requestParser.parse(json);
        } catch (IOException e) {
            actual = e;
            assertEquals("Expected IOException with message, but received", expected.getMessage(), actual.getMessage());
        }
        assertNotNull("Expected IOException but none was thrown", actual);
    }

    @Test
    public void testInputMalformedDate() throws IOException, ParseException {
        Exception actual = null;
        String json = "{\n" +
                "  \"name\": \"Test Task One\",\n" +
                "  \"status\": \"open\",\n" +
                "  \"assignee-id\": \"34\",\n" +
                "  \"assignee-username\": \"Test assignee\",\n" +
                "  \"assignee-email\": \"testassignee@riskgap.ru\",\n" +
                "  \"due\": \"I am a wrong Date and I am not ashamed\",\n" +
                "  \"risk-reference\": \"test risk reference\",\n" +
                "  \"target-system\": \"TFS\"\n" +
                "}";

        try {
            requestParser.parse(json);
        } catch (IOException e) {
            actual = e;
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            assertTrue(stringWriter.toString().contains("Unparseable date"));
        }
        assertNotNull("Expected ParseException but none was thrown", actual);
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
           actual = requestParser.parseTfsGetWorkItemHistoryResponseJson(response);
        } catch (IOException e) {
            assertNull("Test failed, expected no exception", e);
        }

        Comment comment1 = new Comment();
        comment1.setUsername("rg");
        comment1.setEmail("RISKGAPWIN\\rg");
        comment1.setText("Adding the necessary spec");
        comment1.setDate(Comment.TFS_DATE_FORMATTER.parse("2015-07-07T09:08:49.31Z"));

        assertTrue("The length of resulting list should be 2", actual.size() == 2);
        assertEquals("Comment is not correct", comment1, actual.get(0));
        assertEquals("Comment is not correct", comment1, actual.get(1));
    }
}
