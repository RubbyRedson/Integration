package ru.riskgap.integration.api.tfs;

import org.junit.Assert;
import org.junit.Test;
import ru.riskgap.integration.models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 10.07.2015.
 */
public class TfsHandlerTest {

	TfsHandler handler = new TfsHandler();

	@Test
	public void testFormFieldValuePair() {
		Task task = new Task();
		task.setName("Test task");
		task.setStatus(Task.Status.CLOSED);
		task.setDescription("Test description");
		task.setUsername("testUser1");
		task.setUserEmail("test1@email.com");
		task.setAssigneeUsername("testUser2");
		task.setAssigneeEmail("test2@email.com");

		List<FieldValuePair> expectedPairs = new ArrayList<>();
		expectedPairs.add(new FieldValuePair(TfsRequestBuilder.TASK_NAME, "Test task", true));
		expectedPairs.add(new FieldValuePair(TfsRequestBuilder.TASK_STATE,
				Task.Status.CLOSED.getStatus(), true));
		expectedPairs.add(new FieldValuePair(TfsRequestBuilder.TASK_DESCR, "Test description", true));
		expectedPairs.add(new FieldValuePair(TfsRequestBuilder.CHANGED_BY, "testUser1 <test1@email.com>", true));
		expectedPairs.add(new FieldValuePair(TfsRequestBuilder.TASK_ASSIGNEE, "testUser2 <test2@email.com>", true));

		List<FieldValuePair> actualPairs = handler.formFieldValuePairs(task, true);
		Assert.assertEquals("", expectedPairs, actualPairs);
	}
}
