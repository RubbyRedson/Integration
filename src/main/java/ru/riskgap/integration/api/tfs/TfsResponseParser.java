package ru.riskgap.integration.api.tfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.riskgap.integration.models.Auth;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.Task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Nikita on 10.07.2015.
 */
@Component
public class TfsResponseParser {
	final static ThreadLocal<SimpleDateFormat> TFS_DATE_FORMATTER = new ThreadLocal<>();

	static {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.'SS'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		TFS_DATE_FORMATTER.set(dateFormat);
	}
	private final ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(TfsResponseParser.class);

	public TfsResponseParser() {
		this(new ObjectMapper());
	}

	public TfsResponseParser(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * @param jsonBody of a TFS response when GET for Work item fields is executed. It converts all of it into a Task instance.
	 * @return Task instance with all of the information from jsonBody
	 * @throws java.io.IOException when input json is malformed
	 */
	public Task parseTfsGetWorkItemFieldsResponseJson(String jsonBody) throws IOException {

		Map<String, Object> jsonOuterMap = objectMapper.readValue(jsonBody, HashMap.class);
		if (logger.isInfoEnabled())
			logger.info("Received json outer body map is " + jsonOuterMap);

		Task result = new Task();

		if (jsonOuterMap == null)
			return result;

		ArrayList<Map> jsonValueList = (ArrayList) jsonOuterMap.get("value");
		if (logger.isInfoEnabled())
			logger.info("Received json value list is " + jsonValueList);

		if (jsonValueList == null || jsonValueList.size() == 0)
			return result;

		Map<String, String> jsonMap = (Map<String, String>) jsonValueList.get(0).get("fields");
		if (logger.isInfoEnabled())
			logger.info("Received json fields map is " + jsonMap);

		result = parseFieldsForTask(jsonMap);

		result.setTaskId(jsonValueList.get(0).get("id").toString());
		return result;
	}

	/**
	 * @param jsonBody of a TFS response when GET for Work item fields is executed. It converts all of it into a Task instance.
	 * @return Task instance with all of the information from jsonBody
	 * @throws java.io.IOException when input json is malformed
	 */
	public Task parseTfsCreateUpdateWorkItemFieldsResponseJson(String jsonBody) throws IOException {

		Map<String, Object> jsonOuterMap = objectMapper.readValue(jsonBody, HashMap.class);
		if (logger.isInfoEnabled())
			logger.info("Received json outer body map is " + jsonOuterMap);

		Task result = new Task();

		Map<String, String> jsonMap = (Map<String, String>) jsonOuterMap.get("fields");
		if (logger.isInfoEnabled())
			logger.info("Received json fields map is " + jsonMap);

		result = parseFieldsForTask(jsonMap);

		result.setTaskId(jsonOuterMap.get("id").toString());
		return result;
	}

	/**
	 * @param jsonMap - 'fields' values from received json as a map
	 * @return Task object
	 */
	private Task parseFieldsForTask(Map<String, String> jsonMap) {
		Task result = new Task();

		if (jsonMap == null)
			return result;

		result.setName(jsonMap.get(TfsRequestBuilder.TASK_NAME));
		result.setStatus(parseTfsTaskState(jsonMap.get(TfsRequestBuilder.TASK_STATE)));
		result.setDescription(jsonMap.get(TfsRequestBuilder.TASK_DESCR));
		String user = jsonMap.get(TfsRequestBuilder.CHANGED_BY);

		if (user != null && !user.isEmpty()) {
			result.setUsername(getTfsUserName(user));
			result.setUserEmail(getTfsUserMail(user));
		}

		String assignee = jsonMap.get(TfsRequestBuilder.TASK_ASSIGNEE);
		if (assignee != null && !assignee.isEmpty()) {
			result.setAssigneeUsername(getTfsUserName(assignee));
			result.setAssigneeEmail(getTfsUserMail(assignee));
		}

		Auth auth = new Auth();
		auth.setTargetSystem(Auth.TargetSystem.TFS);
		result.setAuth(auth);

		return result;
	}


	/**
	 * @param jsonBody of a TFS response when GET for Work Item history is executed. It converts all of it into a Task instance.
	 * @return Task instance with all of the information from jsonBody
	 * @throws IOException when input json is malformed
	 */
	public List<Comment> parseTfsGetWorkItemHistoryResponseJson(String jsonBody) throws IOException {

		Map<String, Object> jsonOuterMap = objectMapper.readValue(jsonBody, HashMap.class);
		if (logger.isInfoEnabled())
			logger.info("Received json outer body map is " + jsonOuterMap);

		List<Comment> result = new ArrayList<>();

		if (jsonOuterMap == null)
			return result;

		ArrayList<Map> jsonValueList = (ArrayList) jsonOuterMap.get("value");
		if (logger.isInfoEnabled())
			logger.info("Received json value list is " + jsonValueList);

		if (jsonValueList == null || jsonValueList.size() == 0)
			return result;

		for (int i = 0; i < jsonValueList.size(); i++) {
			Map<String, Object> jsonMap = (Map<String, Object>) jsonValueList.get(i);
			if (logger.isInfoEnabled())
				logger.info("Received json fields map is " + jsonMap);

			if (jsonMap != null) {
				Comment comment = new Comment();
				String text = (String) jsonMap.get("value");
				comment.setText(text);
				String fullName = ((Map<String, String>) jsonMap.get("revisedBy")).get("name");
				String name = getTfsUserName(fullName);
				comment.setUsername(name);
				String mail = getTfsUserMail(fullName);
				comment.setEmail(mail);
				String id = (String) jsonMap.get("url");
				String date = (String) jsonMap.get("revisedDate");
				if (date != null) {
					try {
						comment.setDate(TFS_DATE_FORMATTER.get().parse(date));
					} catch (ParseException e) {
						logger.warn("Error while parsing date " + date + " for comment " + id + ", wrong format");
					}
				}
				result.add(comment);
			}
		}

		return result;
	}

	public String getTfsUserName(String fullUserId) {
		if (fullUserId != null) {
			String name = fullUserId.substring(0, fullUserId.lastIndexOf('<') - 1);
			return name;
		}
		return null;
	}

	public String getTfsUserMail(String fullUserId) {
		if (fullUserId != null) {
			String username = fullUserId.substring(0, fullUserId.lastIndexOf('<') - 1);
			String mail = fullUserId.substring(username.length());
			return mail.substring(2, mail.length() - 1); //remove < and >;
		}
		return null;
	}

	public Task.Status parseTfsTaskState(String status) {
		switch (status.toLowerCase()) {
			case "to do":
				return Task.Status.OPEN;
			case "in progress":
				return Task.Status.IN_PROGRESS;
			case "done":
			case "removed":
				return Task.Status.RESOLVED;
			default:
				return null;
		}
	}
}
