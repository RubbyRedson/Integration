package ru.riskgap.integration.api.tfs;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Nikita on 07.07.2015.
 */
public class TfsUpdateTaskRequestBuildingTest {
	@Test
	public void testBuildUpdateUrl() {
		String url = "http://riskgapwin:8080/tfs/NIGU Test Collection";
		String id = "2";
		String actual = TfsRequestBuilder.buildUpdateUrl(url, id);

		String expected = "http://riskgapwin:8080/tfs/NIGU Test Collection/_apis/wit/workitems/2?api-version=1.0";

		Assert.assertEquals("Test build of TFS Update url failed", expected, actual);
	}

	@Test
	public void testEmptyUpdateUrl() {
		String url = "";
		String id = "2";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildUpdateUrl(url, id);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("URL must not be empty");

		Assert.assertEquals("Test build of TFS Update url with empty url failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testNullUpdateUrl() {
		String url = null;
		String id = "2";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildUpdateUrl(url, id);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("URL must not be empty");

		Assert.assertEquals("Test build of TFS Update url with null url failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testEmptyUpdateId() {
		String url = "a";
		String id = "";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildUpdateUrl(url, id);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("Id of the task must not be empty");

		Assert.assertEquals("Test build of TFS Update url with empty id failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testNullUpdateId() {
		String url = "a";
		String id = null;
		Exception actual = null;

		try {
			TfsRequestBuilder.buildUpdateUrl(url, id);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("Id of the task must not be empty");

		Assert.assertEquals("Test build of TFS Update url with null id failed", expected.getMessage(), actual.getMessage());
	}


	@Test
	public void testUpdateIdNotANumber() {
		String url = "a";
		String id = "b";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildUpdateUrl(url, id);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("For input string: \"b\"");

		Assert.assertEquals("Test build of TFS GET url with empty id failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testEmptyFieldsUpdate() {
		String actual = TfsRequestBuilder.buildUpdateRequestBody(new ArrayList<FieldValuePair>());

		String expected = "[\n"  +
				"]";

		Assert.assertEquals("Test build of TFS Task update body failed", expected, actual);
	}

}
