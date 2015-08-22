package ru.riskgap.integration.api.tfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.riskgap.integration.models.Comment;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * A class that has all the necessary methods to build requests to TFS
 * Created by Nikita on 04.07.2015.
 */
public class TfsRequestBuilder {
	private final static Logger logger = LoggerFactory.getLogger(TfsRequestBuilder.class);

	private final static ThreadLocal<SimpleDateFormat> TFS_DATE_FORMATTER = new ThreadLocal<>();

	static {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.'SS'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		TFS_DATE_FORMATTER.set(dateFormat);
	}

	public static final String GET_TASK_FIELDS_URL = "{0}/_apis/wit/WorkItems?ids={1}&fields={2}&api-version=1.0";
	public static final String GET_HISTORY_URL = "{0}/_apis/wit/workitems/{1}/history?api-version=1.0";
	public static final String UPDATE_URL = "{0}/_apis/wit/workitems/{1}?api-version=1.0"; // 2 is WI Ids
	public static final String CREATE_URL = "{0}/_apis/wit/workitems/$Task?api-version=1.0"; // Task type by default

	private static final String DEFAULT_FIELDS = "System.Title,System.State,System.Description,System.ChangedBy,System.AssignedTo";

	public static final String TASK_ID = "System.Id"; 				// Id of the task
	public static final String TASK_NAME = "System.Title"; 			// Name of the task
	public static final String TASK_STATE = "System.State"; 		// State of the task
	public static final String TASK_DESCR = "System.Description"; 	// Description of the task
	public static final String CREATED_BY = "System.CreatedBy"; 	// user who created the task
	public static final String CHANGED_BY = "System.ChangedBy"; 	// user who changed the task
	public static final String TASK_ASSIGNEE = "System.AssignedTo"; // Assignee of the task
	public static final String COMMENT = "System.History"; 			// Comment to the task
	public static final String TASK_TYPE = "System.WorkItemType"; 	// Task or Bug

	private static final String OP_ADD = "\t\t\"op\" : \"add\",\n"; // operation to add new field to task
	private static final String OP_REPLACE = "\t\t\"op\" : \"replace\",\n"; // operation to replace task field value
	private static final String OP_TEST = "\t\t\"op\" : \"test\",\n"; // operation to test field value

	private static final String PATH_TO_THE_FIELD = "\t\t\"path\" : \"{0}\",\n"; // path to field
	private static final String FIELD_VALUE = "\t\t\"value\" : \"{0}\"\n"; // value of the field

	private static final String BEGIN_BODY = "[\n";
	private static final String END_BODY = "]";
	private static final String BEGIN_ELEMENT = "\t{\n";
	private static final String CLOSE_ELEMENT = "\t},\n";

	/**
	 * @param url - full url of TFS project including the collection
	 * @param id - task id in TFS
	 * @param fields - the fields of the task that should be read from TFS
	 * @return url to send a GET request to, to receive information about a task
	 */
	public static String buildGetUrlForTaskFields(String url, String id, String... fields) {
		if (url == null || url.isEmpty())
			throw new IllegalArgumentException("URL must not be empty");

		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("Id of the task must not be empty");

		String preparedFields = null;

		if (fields.length > 0) {
			StringBuilder prepFields = new StringBuilder();
			for (String field : fields) {
				prepFields.append(field);
				prepFields.append(',');
			}
			prepFields.deleteCharAt(prepFields.length() - 1); // remove , after last field
			preparedFields = prepFields.toString();
		} else  {
			preparedFields = DEFAULT_FIELDS;
		}

		return MessageFormat.format(GET_TASK_FIELDS_URL, url, new BigInteger(id), preparedFields); // validate that id is just a number
	}

	/**
	 * @param url - full url of TFS project including the collection
	 * @param id - task id in TFS
	 * @return url to send requests to TFS for getting all of the comments
	 */
	public static String buildGetUrlForWorkItemHistory(String url, String id) {
		if (url == null || url.isEmpty())
			throw new IllegalArgumentException("URL must not be empty");

		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("Id of the task must not be empty");

		return MessageFormat.format(GET_HISTORY_URL, url, new BigInteger(id)); // validate that id is just a number
	}

	/**
	 * @param url - full url of TFS project including the collection
	 * @param id - task id in TFS
	 * @return url to send requests to TFS for updating existing tasks
	 */
	public static String buildUpdateUrl(String url, String id) {
		if (url == null || url.isEmpty())
			throw new IllegalArgumentException("URL must not be empty");

		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("Id of the task must not be empty");

		return MessageFormat.format(UPDATE_URL, url, new BigInteger(id)); // id to number to check it
	}

	/**
	 * @param url - full url of TFS project including the collection
	 * @return url to send requests to TFS for creating new tasks
	 */
	public static String buildCreateUrl(String url) {
		if (url == null || url.isEmpty())
			throw new IllegalArgumentException("URL must not be empty");

		return MessageFormat.format(CREATE_URL, url);
	}

	/**
	 * @param fields - the fields  of the task that should be updated in TFS, wrapped in FieldValuePair
	 * @return JSON body of a request to modify the fields of a task
	 */
	public static String buildUpdateRequestBody(List<FieldValuePair> fields) {
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
		prepFields.deleteCharAt(prepFields.length() - 1); // remove , after last field
		prepFields.append(END_BODY);
		return prepFields.toString();
	}

	/**
	 * @param fields - the fields  of the task that should be created in TFS, wrapped in FieldValuePair
	 * @return JSON body of a request to create a task with some fields
	 */
	public static String buildCreateRequestBody(List<FieldValuePair> fields) {
		StringBuilder prepFields = new StringBuilder();
		prepFields.append(BEGIN_BODY);
		for (FieldValuePair field : fields) {
			prepFields.append(BEGIN_ELEMENT);
			prepFields.append(OP_ADD);
			prepFields.append(MessageFormat.format(PATH_TO_THE_FIELD, "/fields/" + field.getField()));
			prepFields.append(MessageFormat.format(FIELD_VALUE, field.getValue()));
			prepFields.append(CLOSE_ELEMENT);
		}
		prepFields.deleteCharAt(prepFields.length() - 1); // remove , after last field
		prepFields.append(END_BODY);
		return prepFields.toString();
	}

	/**
	 * @param comment - a comment to be added
	 * @return JSON body of a request to add a comment to a task
	 */
	public static String buildUpdateCommentBody(Comment comment) {
		StringBuilder prepFields = new StringBuilder();
		prepFields.append(BEGIN_BODY);
		prepFields.append(BEGIN_ELEMENT);
		prepFields.append(OP_ADD);
		prepFields.append(MessageFormat.format(PATH_TO_THE_FIELD, "/fields/" + COMMENT));
		prepFields.append(MessageFormat.format(FIELD_VALUE, comment.getText() + "\n Written by "
				+ comment.getUsername() + "\n Originally written on " + TFS_DATE_FORMATTER.get().format(comment.getDate())));
		prepFields.append(CLOSE_ELEMENT);
		prepFields.deleteCharAt(prepFields.length() - 1); // remove , after last field
		prepFields.append(END_BODY);
		return prepFields.toString();
	}
}
