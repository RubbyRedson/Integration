package ru.riskgap.integration.models;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

/**
 * Created by Nikita on 03.07.2015.
 */
public class TaskEqualsTest {

    @Test
    public void testEqualTasksFull() throws ParseException {

        Task first = new TaskBuilder()
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

        Task second = new TaskBuilder()
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

        Assert.assertTrue("Tasks Equals test failed. Tasks should have been equal:", first.equals(second));
        Assert.assertTrue("Tasks Equals test failed. Tasks should have been equal:", second.equals(first));
    }

    @Test
    public void testEqualTasksPartial() throws ParseException {
        Task first = new TaskBuilder()
                .setName("Test Task One")
                .setStatus(Task.Status.OPEN)
                .setAssigneeId("34")
                .setAssigneeUsername("Test assignee")
                .setAssigneeEmail("testassignee@riskgap.ru")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"))
                .setRiskRef("test risk reference")
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.TFS)
                        .build())
                .build();

        Task second = new TaskBuilder()
                .setName("Test Task One")
                .setStatus(Task.Status.OPEN)
                .setAssigneeId("34")
                .setAssigneeUsername("Test assignee")
                .setAssigneeEmail("testassignee@riskgap.ru")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"))
                .setRiskRef("test risk reference")
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.TFS)
                        .build())
                .build();

        Assert.assertTrue("Tasks Equals test failed. Tasks should have been equal:", first.equals(second));
        Assert.assertTrue("Tasks Equals test failed. Tasks should have been equal:", second.equals(first));
    }


    @Test
    public void testDifferentTasksFull() throws ParseException {
        Task first = new TaskBuilder()
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

        Task second = new TaskBuilder()
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
                        .setTargetSystem(Auth.TargetSystem.TFS)
                        .build())
                .build();


        Assert.assertTrue("Tasks Equals test failed. Tasks shouldn't have been equal:", !first.equals(second));
        Assert.assertTrue("Tasks Equals test failed. Tasks shouldn't have been equal:", !second.equals(first));
    }

    @Test
    public void testDifferentTasksPartial() throws ParseException {
        Task first = new TaskBuilder()
                .setName("Test Task One")
                .setStatus(Task.Status.OPEN)
                .setAssigneeId("34")
                .setAssigneeUsername("Test assignee")
                .setAssigneeEmail("testassignee@riskgap.ru")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"))
                .setRiskRef("test risk reference")
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.TFS)
                        .build())
                .build();

        Task second = new TaskBuilder()
                .setName("Test Task Two")
                .setStatus(Task.Status.CLOSED)
                .setAssigneeId("42")
                .setAssigneeUsername("Test assignee 2")
                .setAssigneeEmail("testassignee2@riskgap.ru")
                .setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("14.02.2015"))
                .setRiskRef("test risk reference 3")
                .setAuth(new AuthBuilder()
                        .setTargetSystem(Auth.TargetSystem.TFS)
                        .build())
                .build();

        Assert.assertTrue("Tasks Equals test failed. Tasks shouldn't have been equal:", !first.equals(second));
        Assert.assertTrue("Tasks Equals test failed. Tasks shouldn't have been equal:", !second.equals(first));
    }
}
