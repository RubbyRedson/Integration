package ru.riskgap.integration.models.tfs;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nikita on 07.07.2015.
 */
public class TfsGetWorkItemFieldsRequestBuildingTest {
	@Test
	public void testBuildGetUrl() {
		String url = "http://riskgapwin:8080/tfs/NIGU Test Collection";
		String id = "2";
		String actual = TfsRequestBuilder.buildGetUrlForTaskFields(url, id, TfsRequestBuilder.TASK_NAME, TfsRequestBuilder.TASK_STATE,
				TfsRequestBuilder.TASK_DESCR, TfsRequestBuilder.TASK_ASSIGNEE);

		String expected = "http://riskgapwin:8080/tfs/NIGU Test Collection/_apis/wit/WorkItems?ids=2&fields=System.Title," +
				"System.State,System.Description,System.AssignedTo&api-version=1.0";

		Assert.assertEquals("Test build of TFS GET url failed", expected, actual);
	}

	@Test
	public void testEmptyGetUrl() {
		String url = "";
		String id = "2";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildGetUrlForTaskFields(url, id, TfsRequestBuilder.TASK_NAME, TfsRequestBuilder.TASK_STATE,
					TfsRequestBuilder.TASK_DESCR, TfsRequestBuilder.TASK_ASSIGNEE);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("URL must not be empty");

		Assert.assertEquals("Test build of TFS GET url with empty url failed", expected.getMessage(), actual.getMessage());
	}


	@Test
	public void testNullGetUrl() {
		String url = null;
		String id = "2";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildGetUrlForTaskFields(url, id, TfsRequestBuilder.TASK_NAME, TfsRequestBuilder.TASK_STATE,
					TfsRequestBuilder.TASK_DESCR, TfsRequestBuilder.TASK_ASSIGNEE);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("URL must not be empty");

		Assert.assertEquals("Test build of TFS GET url with empty url failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testEmptyGetId() {
		String url = "a";
		String id = "";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildGetUrlForTaskFields(url, id, TfsRequestBuilder.TASK_NAME, TfsRequestBuilder.TASK_STATE,
					TfsRequestBuilder.TASK_DESCR, TfsRequestBuilder.TASK_ASSIGNEE);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("Id of the task must not be empty");

		Assert.assertEquals("Test build of TFS GET url with empty id failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testNullGetId() {
		String url = "a";
		String id = null;
		Exception actual = null;

		try {
			TfsRequestBuilder.buildGetUrlForTaskFields(url, id, TfsRequestBuilder.TASK_NAME, TfsRequestBuilder.TASK_STATE,
					TfsRequestBuilder.TASK_DESCR, TfsRequestBuilder.TASK_ASSIGNEE);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("Id of the task must not be empty");

		Assert.assertEquals("Test build of TFS GET url with empty id failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testGetIdNotANumber() {
		String url = "a";
		String id = "b";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildGetUrlForTaskFields(url, id, TfsRequestBuilder.TASK_NAME, TfsRequestBuilder.TASK_STATE,
					TfsRequestBuilder.TASK_DESCR, TfsRequestBuilder.TASK_ASSIGNEE);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("For input string: \"b\"");

		Assert.assertEquals("Test build of TFS GET url with empty id failed", expected.getMessage(), actual.getMessage());
	}

}
