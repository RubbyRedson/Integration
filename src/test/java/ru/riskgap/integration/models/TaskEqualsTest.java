package ru.riskgap.integration.models;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

/**
 * Created by Nikita on 03.07.2015.
 */
public class TaskEqualsTest {

	@Test
	public void testEqualTasksFull() {
		Task first = new Task();
		first.setName("Test Task One");
		first.setStatus(Task.Status.OPEN);
		first.setDescription("Test Task Description");
		first.setUserId("42");
		first.setUsername("Test user");
		first.setUserEmail("testuser@riskgap.ru");
		first.setAssigneeId("34");
		first.setAssigneeUsername("Test assignee");
		first.setAssigneeEmail("testassignee@riskgap.ru");
		try {
			first.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		first.setRiskRef("test risk reference");
		Auth auth = new Auth();
		auth.setTargetSystem(Auth.TargetSystem.MS_PROJECT);
		first.setAuth(auth);

		Task second = new Task();
		second.setName("Test Task One");
		second.setStatus(Task.Status.OPEN);
		second.setDescription("Test Task Description");
		second.setUserId("42");
		second.setUsername("Test user");
		second.setUserEmail("testuser@riskgap.ru");
		second.setAssigneeId("34");
		second.setAssigneeUsername("Test assignee");
		second.setAssigneeEmail("testassignee@riskgap.ru");
		try {
			second.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		second.setRiskRef("test risk reference");
		Auth auth2 = new Auth();
		auth2.setTargetSystem(Auth.TargetSystem.MS_PROJECT);
		second.setAuth(auth2);

		Assert.assertTrue("Tasks Equals test failed. Tasks should have been equal:", first.equals(second));
		Assert.assertTrue("Tasks Equals test failed. Tasks should have been equal:", second.equals(first));
	}

	@Test
	public void testEqualTasksPartial() {
		Task first = new Task();
		first.setName("Test Task One");
		first.setStatus(Task.Status.OPEN);
		first.setAssigneeId("34");
		first.setAssigneeUsername("Test assignee");
		first.setAssigneeEmail("testassignee@riskgap.ru");
		try {
			first.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		first.setRiskRef("test risk reference");
		Auth auth1 = new Auth();
		auth1.setTargetSystem(Auth.TargetSystem.TFS);
		first.setAuth(auth1);

		Task second = new Task();
		second.setName("Test Task One");
		second.setStatus(Task.Status.OPEN);
		second.setAssigneeId("34");
		second.setAssigneeUsername("Test assignee");
		second.setAssigneeEmail("testassignee@riskgap.ru");
		try {
			second.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		second.setRiskRef("test risk reference");
		Auth auth2 = new Auth();
		auth2.setTargetSystem(Auth.TargetSystem.TFS);
		second.setAuth(auth2);

		Assert.assertTrue("Tasks Equals test failed. Tasks should have been equal:", first.equals(second));
		Assert.assertTrue("Tasks Equals test failed. Tasks should have been equal:", second.equals(first));
	}


	@Test
	public void testDifferentTasksFull() {
		Task first = new Task();
		first.setName("Test Task One");
		first.setStatus(Task.Status.OPEN);
		first.setDescription("Test Task Description");
		first.setUserId("42");
		first.setUsername("Test user");
		first.setUserEmail("testuser@riskgap.ru");
		first.setAssigneeId("34");
		first.setAssigneeUsername("Test assignee");
		first.setAssigneeEmail("testassignee@riskgap.ru");
		try {
			first.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		first.setRiskRef("test risk reference");
		Auth auth = new Auth();
		auth.setTargetSystem(Auth.TargetSystem.MS_PROJECT);
		first.setAuth(auth);

		Task second = new Task();
		second.setName("Test Task Two");
		second.setStatus(Task.Status.CLOSED);
		second.setDescription("Test Task2 Description");
		second.setUserId("423");
		second.setUsername("Test user 2");
		second.setUserEmail("testuser2@riskgap.ru");
		second.setAssigneeId("42");
		second.setAssigneeUsername("Test assignee 2");
		second.setAssigneeEmail("testassignee2@riskgap.ru");
		try {
			second.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("14.02.2015"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		second.setRiskRef("test risk reference 2");
		Auth auth2 = new Auth();
		auth2.setTargetSystem(Auth.TargetSystem.TFS);
		second.setAuth(auth2);

		Assert.assertTrue("Tasks Equals test failed. Tasks shouldn't have been equal:", !first.equals(second));
		Assert.assertTrue("Tasks Equals test failed. Tasks shouldn't have been equal:", !second.equals(first));
	}

	@Test
	public void testDifferentTasksPartial() {
		Task first = new Task();
		first.setName("Test Task One");
		first.setStatus(Task.Status.OPEN);
		first.setAssigneeId("34");
		first.setAssigneeUsername("Test assignee");
		first.setAssigneeEmail("testassignee@riskgap.ru");
		try {
			first.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("12.02.2015"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		first.setRiskRef("test risk reference");
		Auth auth = new Auth();
		auth.setTargetSystem(Auth.TargetSystem.TFS);
		first.setAuth(auth);

		Task second = new Task();
		second.setName("Test Task Two");
		second.setStatus(Task.Status.CLOSED);
		second.setDescription("Test Task2 Description");
		second.setUserId("423");
		second.setUsername("Test user 2");
		second.setUserEmail("testuser2@riskgap.ru");
		second.setAssigneeId("42");
		second.setAssigneeUsername("Test assignee 2");
		second.setAssigneeEmail("testassignee2@riskgap.ru");
		try {
			second.setDue(CustomJsonDateDeserializer.DATE_FORMATTER.parse("14.02.2015"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		second.setRiskRef("test risk reference 2");
		Auth auth2 = new Auth();
		auth2.setTargetSystem(Auth.TargetSystem.TFS);
		second.setAuth(auth);

		Assert.assertTrue("Tasks Equals test failed. Tasks shouldn't have been equal:", !first.equals(second));
		Assert.assertTrue("Tasks Equals test failed. Tasks shouldn't have been equal:", !second.equals(first));
	}
}
