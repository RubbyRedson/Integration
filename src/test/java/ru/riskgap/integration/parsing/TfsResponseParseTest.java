package ru.riskgap.integration.parsing;

import org.junit.Test;
import ru.riskgap.integration.models.TargetSystemEnum;
import ru.riskgap.integration.models.Task;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Nikita on 05.07.2015.
 */
public class TfsResponseParseTest {
	//unit test, no spring autowiring
	RequestParser requestParser = new RequestParser();


	private void test(String input, Task expected) throws IOException {
		Task actual = requestParser.parseTfsGetWorkItemFieldsResponseJson(input);
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
				"         \"System.State\": \"To Do\",\n" +
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
		expected.setStatus("To Do");
		expected.setDescription("Test descr");
		expected.setUsername("rg");
		expected.setUserEmail("RISKGAPWIN\\rg");
		expected.setAssigneeUsername("rg");
		expected.setAssigneeEmail("RISKGAPWIN\\rg");
//		try {
//			expected.setDue(Task.DATE_FORMATTER.parse("12.02.2015"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		expected.setRiskRef("test risk reference");
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
				"   \"count\": 1,\n" +
				"   \"value\": [   {\n" +
				"      \"id\": 2,\n" +
				"      \"rev\": 2,\n" +
				"      \"fields\":       {\n" +
				"         \"System.State\": \"To Do\",\n" +
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
		expected1.setStatus("To Do");
		expected1.setDescription("Test descr");
		expected1.setUsername("rg");
		expected1.setUserEmail("RISKGAPWIN\\rg");
		expected1.setAssigneeUsername("rg");
		expected1.setAssigneeEmail("RISKGAPWIN\\rg");
//		try {
//			expected1.setDue(Task.DATE_FORMATTER.parse("12.02.2015"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		expected1.setRiskRef("test risk reference");
		expected1.setTargetSystem(TargetSystemEnum.TFS);

		String json2 = "{\n" +
				"   \"count\": 1,\n" +
				"   \"value\": [   {\n" +
				"      \"id\": 3,\n" +
				"      \"rev\": 4,\n" +
				"      \"fields\":       {\n" +
				"         \"System.State\": \"To Do\",\n" +
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
		expected2.setStatus("To Do");
		expected2.setDescription("Test descr 2");
		expected2.setUsername("rg");
		expected2.setUserEmail("RISKGAPWIN\\rg");
		expected2.setAssigneeUsername("rg");
		expected2.setAssigneeEmail("RISKGAPWIN\\rg");
//		try {
//			expected2.setDue(Task.DATE_FORMATTER.parse("14.02.2015"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		expected2.setRiskRef("test risk reference 2");
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
				"   \"count\": 1,\n" +
				"   \"value\": [   {\n" +
				"      \"id\": 2,\n" +
				"      \"rev\": 2,\n" +
				"      \"fields\":       {\n" +
				"         \"System.State\": \"To Do\",\n" +
				"         \"System.Title\": \"Test Task\",\n" +
				"         \"System.Description\": \"Test descr\"\n" +
				"      },\n" +
				"      \"url\": \"http://riskgapwin:8080/tfs/NIGU%20Test%20Collection/_apis/wit/workItems/2\"\n" +
				"   }]\n" +
				"}";

		Task expected = new Task();
		expected.setName("Test Task");
		expected.setStatus("To Do");
		expected.setDescription("Test descr");
//		try {
//			expected.setDue(Task.DATE_FORMATTER.parse("12.02.2015"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		expected.setRiskRef("test risk reference");
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
			requestParser.parseTfsGetWorkItemFieldsResponseJson(json);
		} catch (IOException e) {
			actual = e;
			assertEquals("Expected IOException with message, but received", expected.getMessage(), actual.getMessage());
		}
		assertNotNull("Expected IOException but none was thrown", actual);
	}

	// todo malformed username/assignee names tests, Due Date?
}
