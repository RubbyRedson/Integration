package ru.riskgap.integration.models.tfs;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nikita on 04.07.2015.
 */
public class TfsRequestBuilderTest {

	@Test
	public void testBuildGetUrl() {
		String url = "http://riskgapwin:8080/tfs/NIGU Test Collection";
		String id = "2";
		String actual = TfsRequestBuilder.buildGetUrl(url, id, TfsRequestBuilder.TASK_NAME,	TfsRequestBuilder.TASK_STATE,
				TfsRequestBuilder.TASK_DESCR, TfsRequestBuilder.TASK_ASSIGNEE);

		String expected = "http://riskgapwin:8080/tfs/NIGU Test Collection/_apis/wit/WorkItems?ids=2&fields=System.Title," +
				"System.State,System.Description,System.AssignedTo&api-version=1.0";

		Assert.assertEquals("Test build of TFS GET url failed", expected, actual);
	}

	@Test
	public void testBuildTaskUpdateBody() {
		FieldValuePair taskName = new FieldValuePair(TfsRequestBuilder.TASK_NAME, "Test Task name", true);
		FieldValuePair taskDescr = new FieldValuePair(TfsRequestBuilder.TASK_DESCR, "Test Task description", false);

		String actual = TfsRequestBuilder.buildUpdateRequestBody(taskName, taskDescr);

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

		String actual = TfsRequestBuilder.buildCreateRequestBody(taskName, taskDescr);

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
	public void testEmptyFieldsUpdate() {
		String actual = TfsRequestBuilder.buildUpdateRequestBody();

		String expected = "[\n"  +
				"]";

		Assert.assertEquals("Test build of TFS Task update body failed", expected, actual);
	}

	@Test
	public void testEmptyFieldsCreate() {
		String actual = TfsRequestBuilder.buildCreateRequestBody();

		String expected = "[\n"  +
				"]";

		Assert.assertEquals("Test build of TFS Task update body failed", expected, actual);
	}
}
