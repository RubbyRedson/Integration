package ru.riskgap.integration.models;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

/**
 * Created by Nikita on 03.07.2015.
 */
public class TaskToJsonTest {
	
	@Test
	public void testTaskToJsonFull() {
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
				"  \"due\": \"29.12.2015\",\n" +
				"  \"risk-reference\": \"test risk reference\",\n" +
				"  \"target-system\": \"MS_PROJECT\"\n" +
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
		try {
			expected.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("29.12.2015"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		expected.setRiskRef("test risk reference");
		expected.setTargetSystem(TargetSystemEnum.MS_PROJECT);

		Assert.assertEquals("Task to JSON full failed", json, expected.toJson());
	}
	
	@Test
	public void testTaskToJsonPartial() {
		String json = "{\n" +
				"  \"name\": \"Test Task One\",\n" +
				"  \"status\": \"In Progress\",\n" +
				"  \"description\": \"null\",\n" +
				"  \"user-id\": \"null\",\n" +
				"  \"username\": \"null\",\n" +
				"  \"user-email\": \"null\",\n" +
				"  \"assignee-id\": \"34\",\n" +
				"  \"assignee-username\": \"Test assignee\",\n" +
				"  \"assignee-email\": \"testassignee@riskgap.ru\",\n" +
				"  \"due\": \"10.10.2015\",\n" +
				"  \"risk-reference\": \"test risk reference\",\n" +
				"  \"target-system\": \"TFS\"\n" +
				"}";

		Task expected = new Task();
		expected.setName("Test Task One");
		expected.setStatus("In Progress");
		expected.setAssigneeId("34");
		expected.setAssigneeUsername("Test assignee");
		expected.setAssigneeEmail("testassignee@riskgap.ru");
		try {
			expected.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("10.10.2015"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		expected.setRiskRef("test risk reference");
		expected.setTargetSystem(TargetSystemEnum.TFS);

		Assert.assertEquals("Task to JSON partial failed", json, expected.toJson());
	}
}
