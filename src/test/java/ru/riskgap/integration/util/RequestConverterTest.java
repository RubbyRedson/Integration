package ru.riskgap.integration.util;

import org.junit.Test;
import ru.riskgap.integration.models.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Test class for parser
 * Created by Nikita on 03.07.2015.
 */

public class RequestConverterTest {
    //unit test, no spring autowiring
    RequestConverter requestConverter = new RequestConverter();

    private void testInputJson(String input, Task expected) throws IOException, ParseException {
        Task actual = requestConverter.fromJSONtoTask(input);
        assertEquals("JSON parsing test", expected, actual);
    }

    @Test
    public void testInputOneRequest() throws ParseException {
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
                "  \"auth\": {\n" +
                "     \"target-system\": \"MS Project\"\n" +
                "  }" +
                "}";

        Task expected = new TaskBuilder()
                .setContainerId("Project X")
                .setTaskId("12")
                .setName("Test Task One")
                .setStatus(Task.Status.OPEN)
                .setDescription("Test Task Description")
                .setUserId("42")
                .setUsername("Test user")
                .setUserEmail("testuser@riskgap.ru")
                .setAssigneeId("34")
                .setAssigneeUsername("Test assignee")
                .setAssigneeEmail("testassignee@riskgap.ru")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"))
                .setRiskRef("test risk reference")
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.MS_PROJECT)
                        .build())
                .build();

        try {
            testInputJson(json, expected);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testInputSeveralRequestsSequentially() throws ParseException {
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
                "  \"auth\": {\n" +
                "    \"target-system\": \"MS Project\"\n" +
                "  }" +
                "}";

        Task expected1 = new TaskBuilder()
                .setName("Test Task One")
                .setStatus(Task.Status.OPEN)
                .setDescription("Test Task Description")
                .setUserId("42")
                .setUsername("Test user")
                .setUserEmail("testuser@riskgap.ru")
                .setAssigneeId("34")
                .setAssigneeUsername("Test assignee")
                .setAssigneeEmail("testassignee@riskgap.ru")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"))
                .setRiskRef("test risk reference")
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.MS_PROJECT)
                        .build())
                .build();

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
                "  \"auth\": {\n" +
                "    \"target-system\": \"MS Project\"\n" +
                "  }" +
                "}";

        Task expected2 = new TaskBuilder()
                .setName("Test Task Two")
                .setStatus(Task.Status.CLOSED)
                .setDescription("Test Task2 Description")
                .setUserId("423")
                .setUsername("Test user 2")
                .setUserEmail("testuser2@riskgap.ru")
                .setAssigneeId("42")
                .setAssigneeUsername("Test assignee 2")
                .setAssigneeEmail("testassignee2@riskgap.ru")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("14.02.2015"))
                .setRiskRef("test risk reference 2")
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.MS_PROJECT)
                        .build())
                .build();

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
    public void testInputPartialTaskRequest() throws ParseException {
        Exception ex = null;
        String json = "{\n" +
                "  \"name\": \"Test Task One\",\n" +
                "  \"status\": \"closed\",\n" +
                "  \"assignee-id\": \"34\",\n" +
                "  \"assignee-username\": \"Test assignee\",\n" +
                "  \"assignee-email\": \"testassignee@riskgap.ru\",\n" +
                "  \"due\": \"12.02.2015\",\n" +
                "  \"risk-reference\": \"test risk reference\",\n" +
                "  \"auth\": {\n" +
                "    \"target-system\": \"MS Project\"\n" +
                "  }" +
                "}";

        Task expected = new TaskBuilder()
                .setName("Test Task One")
                .setStatus(Task.Status.CLOSED)
                .setAssigneeId("34")
                .setAssigneeUsername("Test assignee")
                .setAssigneeEmail("testassignee@riskgap.ru")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"))
                .setRiskRef("test risk reference")
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.MS_PROJECT)
                        .build())
                .build();
        try {
            testInputJson(json, expected);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testInputTaskWithComments() throws ParseException {
        Exception ex = null;
        String json = "{\n" +
                "  \"name\": \"andrey\",\n" +
                "  \"status\": \"closed\",\n" +
                "  \"due\": \"12.08.2015\",\n" +
                "  \"comments\": [\n" +
                "    {\"text\": \"First Comment!\", \"date\": \"13.09.2015\"},\n" +
                "    {\"text\": \"Second Comment!\", \"date\": \"14.09.2015\"}\n" +
                "  ] \n" +
                "}";
        Task expected = new TaskBuilder()
                .setName("andrey")
                .setStatus(Task.Status.CLOSED)
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.08.2015"))
                .setComments(Arrays.asList(
                        new Comment(CustomJsonDateDeserializer.DATE_FORMATTER.parse("13.09.2015"), "First Comment!"),
                        new Comment(CustomJsonDateDeserializer.DATE_FORMATTER.parse("14.09.2015"), "Second Comment!")))
                .build();
        try {
            testInputJson(json, expected);
        } catch (IOException e) {
            e.printStackTrace();
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testInputTaskWithEmptyComments() throws ParseException {
        Exception ex = null;
        String json = "{\n" +
                "  \"name\": \"andrey\",\n" +
                "  \"status\": \"closed\",\n" +
                "  \"due\": \"12.08.2015\",\n" +
                "  \"comments\": []\n" +
                "}";
        Task expected = new TaskBuilder()
                .setName("andrey")
                .setStatus(Task.Status.CLOSED)
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.08.2015"))
                .setComments(new ArrayList<Comment>())
                .build();
        try {
            testInputJson(json, expected);
        } catch (IOException e) {
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
            requestConverter.fromJSONtoTask(json);
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
                "  \"auth\": {\n" +
                "    \"target-system\": \"MS Project\"\n" +
                "  }," +
                "}";

        try {
            requestConverter.fromJSONtoTask(json);
        } catch (IOException e) {
            actual = e;
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            assertTrue("Expected to contain Unparseable date, but was: " + stringWriter.toString(),
                    stringWriter.toString().contains("Unparseable date"));
        }
        assertNotNull("Expected ParseException but none was thrown", actual);
    }

}
