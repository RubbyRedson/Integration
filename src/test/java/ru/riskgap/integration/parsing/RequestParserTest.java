package ru.riskgap.integration.parsing;

import org.junit.Test;
import ru.riskgap.integration.models.TargetSystemEnum;
import ru.riskgap.integration.models.Task;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for parser
 * Created by Nikita on 03.07.2015.
 */

public class RequestParserTest {
    //unit test, no spring autowiring
    RequestParser requestParser = new RequestParser();

    private void test(String input, Task expected) throws IOException {
        Task actual = requestParser.parse(input);
        assertEquals("JSON parsing test failed", expected, actual);
    }

    @Test
    public void testOneRequest() {
        String json = "{\n" +
                "  \"name\": \"Test Task One\",\n" +
                "  \"status\": \"In Progress\",\n" +
                "  \"description\": \"Test Task Description\",\n" +
                "  \"user-id\": \"42\",\n" +
                "  \"username\": \"Test user\",\n" +
                "  \"user-email\": \"testuser@riskgap.ru\",\n" +
                "  \"assignee-id\": \"34\",\n" +
                "  \"assignee-username\": \"Test assignee\",\n" +
                "  \"assignee-email\": \"testassignee@riskgap.ru\",\n" +
                "  \"due\": \"12-02-2015\",\n" +
                "  \"risk-reference\": \"test risk reference\",\n" +
                "  \"target-system\": \"TFS\"\n" +
                "}";

        Task expected = new Task();
        expected.setName("Test Task One");
        expected.setStatus("In Progress");
        expected.setDescription("Test Task Description");
        expected.setUserId("42");
        expected.setUsername("Test user");
        expected.setUserEmail("testuser@riskgap.ru");
        expected.setAssigneeId("34");
        expected.setAssigneeUsername("Test assignee");
        expected.setAssigneeEmail("testassignee@riskgap.ru");
        expected.setDue("12-02-2015");
        expected.setRiskRef("test risk reference");
        expected.setTargetSystem(TargetSystemEnum.TFS);

        try {
            test(json, expected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSeveralRequestsSequentially() {
        String json1 = "{\n" +
                "  \"name\": \"Test Task One\",\n" +
                "  \"status\": \"In Progress\",\n" +
                "  \"description\": \"Test Task Description\",\n" +
                "  \"user-id\": \"42\",\n" +
                "  \"username\": \"Test user\",\n" +
                "  \"user-email\": \"testuser@riskgap.ru\",\n" +
                "  \"assignee-id\": \"34\",\n" +
                "  \"assignee-username\": \"Test assignee\",\n" +
                "  \"assignee-email\": \"testassignee@riskgap.ru\",\n" +
                "  \"due\": \"12-02-2015\",\n" +
                "  \"risk-reference\": \"test risk reference\",\n" +
                "  \"target-system\": \"MS_PROJECT\"\n" +
                "}";

        Task expected1 = new Task();
        expected1.setName("Test Task One");
        expected1.setStatus("In Progress");
        expected1.setDescription("Test Task Description");
        expected1.setUserId("42");
        expected1.setUsername("Test user");
        expected1.setUserEmail("testuser@riskgap.ru");
        expected1.setAssigneeId("34");
        expected1.setAssigneeUsername("Test assignee");
        expected1.setAssigneeEmail("testassignee@riskgap.ru");
        expected1.setDue("12-02-2015");
        expected1.setRiskRef("test risk reference");
        expected1.setTargetSystem(TargetSystemEnum.MS_PROJECT);

        String json2 = "{\n" +
                "  \"name\": \"Test Task Two\",\n" +
                "  \"status\": \"Completed\",\n" +
                "  \"description\": \"Test Task2 Description\",\n" +
                "  \"user-id\": \"423\",\n" +
                "  \"username\": \"Test user 2\",\n" +
                "  \"user-email\": \"testuser2@riskgap.ru\",\n" +
                "  \"assignee-id\": \"42\",\n" +
                "  \"assignee-username\": \"Test assignee 2\",\n" +
                "  \"assignee-email\": \"testassignee2@riskgap.ru\",\n" +
                "  \"due\": \"14-02-2015\",\n" +
                "  \"risk-reference\": \"test risk reference 2\",\n" +
                "  \"target-system\": \"TFS\"\n" +
                "}";

        Task expected2 = new Task();
        expected2.setName("Test Task Two");
        expected2.setStatus("Completed");
        expected2.setDescription("Test Task2 Description");
        expected2.setUserId("423");
        expected2.setUsername("Test user 2");
        expected2.setUserEmail("testuser2@riskgap.ru");
        expected2.setAssigneeId("42");
        expected2.setAssigneeUsername("Test assignee 2");
        expected2.setAssigneeEmail("testassignee2@riskgap.ru");
        expected2.setDue("14-02-2015");
        expected2.setRiskRef("test risk reference 2");
        expected2.setTargetSystem(TargetSystemEnum.TFS);

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
                "  \"name\": \"Test Task One\",\n" +
                "  \"status\": \"In Progress\",\n" +
                "  \"assignee-id\": \"34\",\n" +
                "  \"assignee-username\": \"Test assignee\",\n" +
                "  \"assignee-email\": \"testassignee@riskgap.ru\",\n" +
                "  \"due\": \"12-02-2015\",\n" +
                "  \"risk-reference\": \"test risk reference\",\n" +
                "  \"target-system\": \"TFS\"\n" +
                "}";

        Task expected = new Task();
        expected.setName("Test Task One");
        expected.setStatus("In Progress");
        expected.setAssigneeId("34");
        expected.setAssigneeUsername("Test assignee");
        expected.setAssigneeEmail("testassignee@riskgap.ru");
        expected.setDue("12-02-2015");
        expected.setRiskRef("test risk reference");
        expected.setTargetSystem(TargetSystemEnum.TFS);

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
            requestParser.parse(json);
        } catch (IOException e) {
            actual = e;
            assertEquals("Expected IOException with message, but received", expected.getMessage(), actual.getMessage());
        }
        assertNotNull("Expected IOException but none was thrown", actual);
    }
}
