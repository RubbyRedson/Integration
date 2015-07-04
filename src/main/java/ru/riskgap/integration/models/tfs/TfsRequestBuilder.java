package ru.riskgap.integration.models.tfs;

import java.text.MessageFormat;

/**
 * Created by Nikita on 04.07.2015.
 */
public class TfsRequestBuilder {
	public static final String GET_URL = "{0}/_apis/wit/WorkItems?ids={1}&fields={2}&api-version=1.0";
	public static final String UPDATE_URL = "{0}/_apis/wit/workitems/{1}?api-version=1.0"; // 2 is WI Ids
	public static final String CREATE_URL = "{0}/_apis/wit/workitems/$Task?api-version=1.0"; // Task type by default

	private static final String FIELDS = "System.Title,System.State,System.Description,System.AssignedTo";

	public static final String TASK_NAME = "System.Title"; // Name of the task
	public static final String TASK_STATE = "System.State"; // State of the task
	public static final String TASK_DESCR = "System.Description"; // Description of the task
	public static final String TASK_ASSIGNEE = "System.AssignedTo"; // Assignee of the task

	private static final String OP_ADD = "\t\t\"op\" : \"add\",\n"; // operation to add new field to task
	private static final String OP_REPLACE = "\t\t\"op\" : \"replace\",\n"; // operation to replace task field value
	private static final String OP_TEST = "\t\t\"op\" : \"test\",\n"; // operation to test field value

	private static final String PATH_TO_THE_FIELD = "\t\t\"path\" : \"{0}\",\n"; // path to field
	private static final String FIELD_VALUE = "\t\t\"value\" : \"{0}\"\n"; // value of the field

	private static final String BEGIN_BODY = "[\n";
	private static final String END_BODY = "]";
	private static final String BEGIN_ELEMENT = "\t{\n";
	private static final String CLOSE_ELEMENT = "\t}\n";

	public static String buildGetUrl(String url, String id, String... fields) {
		if (url == null || url.isEmpty())
			throw new IllegalArgumentException("URL must not be empty");

		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("Id of the task must not be empty");

		StringBuilder prepFields = new StringBuilder();
		for (String field : fields) {
			//todo add some basic validation?
			prepFields.append(field);
			prepFields.append(',');
		}
		prepFields.deleteCharAt(prepFields.length() - 1); // remove , after last field
		return MessageFormat.format(GET_URL, url, id, prepFields.toString());
	}

	public static String buildUpdateUrl(String url, String id) {
		if (url == null || url.isEmpty())
			throw new IllegalArgumentException("URL must not be empty");

		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("Id of the task must not be empty");

		return MessageFormat.format(UPDATE_URL, url, id);
	}

	public static String buildCreateUrl(String url) {
		if (url == null || url.isEmpty())
			throw new IllegalArgumentException("URL must not be empty");

		return MessageFormat.format(CREATE_URL, url);
	}

	public static String buildUpdateRequestBody(FieldValuePair... fields) {
		StringBuilder prepFields = new StringBuilder();
		prepFields.append(BEGIN_BODY);
		for (FieldValuePair field : fields) {
			prepFields.append(BEGIN_ELEMENT);
			if (field.isNewField()) {
				prepFields.append(OP_ADD);
			} else {
				prepFields.append(OP_REPLACE);
			}
			prepFields.append(MessageFormat.format(PATH_TO_THE_FIELD, "/fields/" + field.getField()));
			prepFields.append(MessageFormat.format(FIELD_VALUE, field.getValue()));
			prepFields.append(CLOSE_ELEMENT);
		}
		prepFields.append(END_BODY);
		return prepFields.toString();
	}

	public static String buildCreateRequestBody(FieldValuePair... fields) {
		StringBuilder prepFields = new StringBuilder();
		prepFields.append(BEGIN_BODY);
		for (FieldValuePair field : fields) {
			prepFields.append(BEGIN_ELEMENT);
			prepFields.append(OP_ADD);
			prepFields.append(MessageFormat.format(PATH_TO_THE_FIELD, "/fields/" + field.getField()));
			prepFields.append(MessageFormat.format(FIELD_VALUE, field.getValue()));
			prepFields.append(CLOSE_ELEMENT);
		}
		prepFields.append(END_BODY);
		return prepFields.toString();
	}
}
