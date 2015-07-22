package ru.riskgap.integration.models;

import org.junit.Test;
import ru.riskgap.integration.util.RequestConverter;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Nikita on 03.07.2015.
 */
public class TaskToJsonTest {

    private static RequestConverter converter = new RequestConverter();

    @Test
    public void testTaskToJson_checkNoAuth() throws Exception {
        Task task = new TaskBuilder()
                .setName("Test Task One")
                .setStatus(Task.Status.OPEN)
                .setDescription("Test Task Description")
                .setUserId("42")
                .setUsername("Test user")
                .setUserEmail("testuser@riskgap.ru")
                .setAssigneeId("34")
                .setAssigneeUsername("Test assignee")
                .setAssigneeEmail("testassignee@riskgap.ru")
                .setAuth(new AuthBuilder()
                        .setUserToken("TOKEN")
                        .setApplicationKey("KEY")
                        .setTargetSystem(Auth.TargetSystem.TFS)
                        .build())
                .build();
        String entity = converter.fromTaskToJSON(task);
        Task parsedTask = converter.fromJSONtoTask(entity);
        assertNull("Task mustn't contain Auth data", parsedTask.getAuth());
    }

    @Test
    public void testTaskToJsonFull() throws Exception {

        Task task = new TaskBuilder()
                .setContainerId("PROJECT")
                .setName("Test Task One")
                .setStatus(Task.Status.OPEN)
                .setDescription("Test Task Description")
                .setUserId("42")
                .setUsername("Test user")
                .setUserEmail("testuser@riskgap.ru")
                .setAssigneeId("34")
                .setAssigneeUsername("Test assignee")
                .setAssigneeEmail("testassignee@riskgap.ru")
                .setRiskRef("http://google.ru")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("29.12.2015"))
                .setTaskId("TASK_ID")
                .setComments(Collections.singletonList(new Comment(
                        CustomJsonDateDeserializer.DATE_FORMATTER.parse("29.12.2015"),
                        "Hello, Comment!"
                )))
                .build();
        String entity = converter.fromTaskToJSON(task);
        Task parsedTask = converter.fromJSONtoTask(entity);
        assertEquals("Task to JSON full failed", task, parsedTask);
    }

    @Test
    public void testTaskToJsonPartial() throws Exception {
        Task task = new TaskBuilder()
                .setContainerId("PROJECT")
                .setName("Test Task One")
                .setStatus(Task.Status.OPEN)
                .setDescription("Test Task Description")
                .setRiskRef("http://google.ru")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("29.12.2015"))
                .setTaskId("TASK_ID")
                .build();
        String entity = converter.fromTaskToJSON(task);
        Task parsedTask = converter.fromJSONtoTask(entity);
        assertEquals("Task to JSON partial failed", task, parsedTask);
    }
}
