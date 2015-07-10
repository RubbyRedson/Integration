package ru.riskgap.integration.api.tfs;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nikita on 07.07.2015.
 */
public class TfsGetWorkItemHistoryRequestBuildingTest {

	@Test
	public void testBuildGetHistoryUrl() {
		String url = "http://riskgapwin:8080/tfs/NIGU Test Collection";
		String id = "2";
		String actual = TfsRequestBuilder.buildGetUrlForWorkItemHistory(url, id);;

		String expected = "http://riskgapwin:8080/tfs/NIGU Test Collection/_apis/wit/workitems/2/history?api-version=1.0";

		Assert.assertEquals("Test build of TFS GET url failed", expected, actual);
	}

	@Test
	public void testEmptyGetHistoryUrl() {
		String url = "";
		String id = "2";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildGetUrlForWorkItemHistory(url, id);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("URL must not be empty");

		Assert.assertEquals("Test build of TFS GET url with empty url failed", expected.getMessage(), actual.getMessage());
	}


	@Test
	public void testNullGetHistoryUrl() {
		String url = null;
		String id = "2";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildGetUrlForWorkItemHistory(url, id);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("URL must not be empty");

		Assert.assertEquals("Test build of TFS GET url with empty url failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testEmptyGetHistoryId() {
		String url = "a";
		String id = "";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildGetUrlForWorkItemHistory(url, id);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("Id of the task must not be empty");

		Assert.assertEquals("Test build of TFS GET url with empty id failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testNullGetHistoryId() {
		String url = "a";
		String id = null;
		Exception actual = null;

		try {
			TfsRequestBuilder.buildGetUrlForWorkItemHistory(url, id);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("Id of the task must not be empty");

		Assert.assertEquals("Test build of TFS GET url with empty id failed", expected.getMessage(), actual.getMessage());
	}
}
