package ru.riskgap.integration.api.tfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.Task;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikita on 10.07.2015.
 */
@Component
public class TfsResponseParser {
	public static final DateFormat TFS_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.'SS'Z'");

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

		if (jsonMap == null)
			return result;

		result.setName(jsonMap.get(TfsRequestBuilder.TASK_NAME));
		result.setStatus(Task.Status.valueOf(jsonMap.get(TfsRequestBuilder.TASK_STATE).toUpperCase()));
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

//        String date = jsonMap.get(Task.TASK_DUE);
//        if (date != null) {
//            result.setDue(Task.DATE_FORMATTER.parse(date));
//        }

//        result.setRiskRef(jsonMap.get(Task.RISK_REF));

		result.setTargetSystem(Task.TargetSystem.TFS);

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
						comment.setDate(TFS_DATE_FORMATTER.parse(date));
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
}
