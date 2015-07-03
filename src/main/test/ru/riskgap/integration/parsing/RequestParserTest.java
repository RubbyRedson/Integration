package ru.riskgap.integration.parsing;

import org.junit.Test;
import ru.riskgap.integration.Task;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test class for parser
 * Created by Nikita on 03.07.2015.
 */

public class RequestParserTest {
    //unit test, no spring autowiring
    RequestParser requestParser = new RequestParser();

    private void test(String input, Task expected) throws IOException {
        Task actual = requestParser.parse(input);
        assertEquals("", expected, actual);
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
                "  \"target-system\": \"tfs\"\n" +
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
        expected.setTargetSystem("tfs");

        try {
            test(json, expected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
