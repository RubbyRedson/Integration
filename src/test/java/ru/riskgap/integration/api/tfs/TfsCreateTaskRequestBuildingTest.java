package ru.riskgap.integration.api.tfs;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 07.07.2015.
 */
public class TfsCreateTaskRequestBuildingTest {
	@Test
	public void testBuildCreateUrl() {
		String url = "http://riskgapwin:8080/tfs/NIGU Test Collection";
		String actual = TfsRequestBuilder.buildCreateUrl(url);

		String expected = "http://riskgapwin:8080/tfs/NIGU Test Collection/_apis/wit/workitems/$Task?api-version=1.0";

		Assert.assertEquals("Test build of TFS Create url failed", expected, actual);
	}

	@Test
	public void testEmptyCreateUrl() {
		String url = "";
		Exception actual = null;

		try {
			TfsRequestBuilder.buildCreateUrl(url);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("URL must not be empty");

		Assert.assertEquals("Test build of TFS Create url with empty url failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testNullCreateUrl() {
		String url = null;
		Exception actual = null;

		try {
			TfsRequestBuilder.buildCreateUrl(url);
		} catch (IllegalArgumentException e) {
			actual = e;
		}

		Exception expected = new IllegalArgumentException("URL must not be empty");

		Assert.assertEquals("Test build of TFS Create url with null url failed", expected.getMessage(), actual.getMessage());
	}

	@Test
	public void testBuildTaskUpdateBody() {
		FieldValuePair taskName = new FieldValuePair(TfsRequestBuilder.TASK_NAME, "Test Task name", true);
		FieldValuePair taskDescr = new FieldValuePair(TfsRequestBuilder.TASK_DESCR, "Test Task description", false);

		List<FieldValuePair> pairs = new ArrayList<>();
		pairs.add(taskName);
		pairs.add(taskDescr);
		String actual = TfsRequestBuilder.buildUpdateRequestBody(pairs);

		String expected = "[\n" +
				"\t{\n" +
				"\t\t\"op\" : \"add\",\n" +
				"		\"path\" : \"/fields/System.Title\",\n" +
				"		\"value\" : \"Test Task name\"\n" +
				"\t}\n" +
				"\t{\n" +
				"		\"op\" : \"replace\",\n" +
				"		\"path\" : \"/fields/System.Description\",\n" +
				"		\"value\" : \"Test Task description\"\n" +
				"\t}\n" +
				"]";

		Assert.assertEquals("Test build of TFS Task update body failed", expected, actual);
	}


	@Test
	public void testBuildTaskCreateBody() {
		FieldValuePair taskName = new FieldValuePair(TfsRequestBuilder.TASK_NAME, "Test Task name", true);
		FieldValuePair taskDescr = new FieldValuePair(TfsRequestBuilder.TASK_DESCR, "Test Task description", true);


		List<FieldValuePair> pairs = new ArrayList<>();
		pairs.add(taskName);
		pairs.add(taskDescr);

		String actual = TfsRequestBuilder.buildCreateRequestBody(pairs);
		String expected = "[\n" +
				"\t{\n" +
				"\t\t\"op\" : \"add\",\n" +
				"		\"path\" : \"/fields/System.Title\",\n" +
				"		\"value\" : \"Test Task name\"\n" +
				"\t}\n" +
				"\t{\n" +
				"		\"op\" : \"add\",\n" +
				"		\"path\" : \"/fields/System.Description\",\n" +
				"		\"value\" : \"Test Task description\"\n" +
				"\t}\n" +
				"]";

		Assert.assertEquals("Test build of TFS Task update body failed", expected, actual);
	}

	@Test
	public void testEmptyFieldsCreate() {
		String actual = TfsRequestBuilder.buildCreateRequestBody(new ArrayList<FieldValuePair>());

		String expected = "[\n"  +
				"]";

		Assert.assertEquals("Test build of TFS Task update body failed", expected, actual);
	}
}
