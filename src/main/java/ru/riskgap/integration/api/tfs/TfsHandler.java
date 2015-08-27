package ru.riskgap.integration.api.tfs;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.riskgap.integration.IntegrationHandler;
import ru.riskgap.integration.exceptions.AuthException;
import ru.riskgap.integration.exceptions.InternalServerException;
import ru.riskgap.integration.exceptions.InvalidInputDataException;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.ApacheHttpClient;

import java.io.IOException;
import java.util.*;

public class TfsHandler implements IntegrationHandler {
    private final static Logger logger = LoggerFactory.getLogger(TfsHandler.class);

    //@Autowired
    TfsResponseParser responseParser = new TfsResponseParser();

    @Override
    public Task createOrUpdateTask(Task task) {
        if (task.getTaskId() != null) { //update
            try {
                return updateTask(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                return createTask(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Task getTaskInformation(Task task) {
        ApacheHttpClient httpClient = new ApacheHttpClient(createHttpClient(task));

        String url = task.getTargetUrl();
        String taskId = task.getTaskId();

        String getFieldsUrl = TfsRequestBuilder.buildGetUrlForTaskFields(url, taskId);
        String getCommentsUrl = TfsRequestBuilder.buildGetUrlForWorkItemHistory(url, taskId);

        try {
            CloseableHttpResponse httpResponse = httpClient.get(getFieldsUrl);

            if (httpResponse.getStatusLine().getStatusCode() == 401)
                throw new AuthException("An error occured during Task information retrieval. The credentials provided were not" +
                        "authorized to modify the task.");

            if (httpResponse.getStatusLine().getStatusCode() == 404)
                throw new InvalidInputDataException("An error occured during Task information retrieval. The project target url was" +
                        "incorrect.");

            if (httpResponse.getStatusLine().getStatusCode() == 400)
                throw new InternalServerException("An error occured during Task information retrieval.");

            Task result = responseParser.parseTfsGetWorkItemFieldsResponseJson(
                    httpClient.extractEntity(httpResponse, false));

            CloseableHttpResponse httpCommentsResponse = httpClient.get(getCommentsUrl);

            if (httpCommentsResponse.getStatusLine().getStatusCode() == 401)
                throw new AuthException("An error occured during Task information retrieval. The credentials provided were not" +
                        "authorized to modify the task.");

            if (httpCommentsResponse.getStatusLine().getStatusCode() == 404)
                throw new InvalidInputDataException("An error occured during Task information retrieval. The project target url was" +
                        "incorrect.");

            if (httpResponse.getStatusLine().getStatusCode() == 400)
                throw new InternalServerException("An error occured during Task information retrieval.");
            List<Comment> commentList = responseParser.parseTfsGetWorkItemHistoryResponseJson(
                    httpClient.extractEntity(httpCommentsResponse, true));

            result.setComments(commentList);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
        return null;
    }

    /**
     * Method that will be called if task must be updated
     * @param task
     * @throws Exception
     * @return changed task
     */
    private Task updateTask(Task task) throws Exception {
        ApacheHttpClient httpClient = new ApacheHttpClient(createHttpClient(task));

        String url = task.getTargetUrl();
        String taskId = task.getTaskId();

        String updateTaskUrl = TfsRequestBuilder.buildUpdateUrl(url, taskId);
        String updateRequestBody = TfsRequestBuilder.buildUpdateRequestBody(formFieldValuePairs(task, true));

        try {
            CloseableHttpResponse httpResponse = httpClient.patch(updateTaskUrl, updateRequestBody,
                    new BasicNameValuePair("Content-Type", "application/json-patch+json"));
            if (httpResponse.getStatusLine().getStatusCode() == 401)
                throw new AuthException("An error occured during Task update. The credentials provided were not" +
                        "authorized to modify the task.");

            if (httpResponse.getStatusLine().getStatusCode() == 404)
                throw new InvalidInputDataException("An error occured during Task update. The project target url was" +
                        "incorrect.");

            if (httpResponse.getStatusLine().getStatusCode() == 400)
                throw new InternalServerException("An error occured during Task update.");
            String response = EntityUtils.toString(httpResponse.getEntity());
            logger.info(response);
            Task result = responseParser.parseTfsCreateUpdateWorkItemFieldsResponseJson(response);
            if (task.getComments() != null && task.getComments().size() > 0) {
                result.setTargetUrl(task.getTargetUrl());
                result.setAuth(task.getAuth());
                result = getTaskInformation(result);
                List<Comment> sortedComments = task.getComments();

                Collections.sort(sortedComments, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment o1, Comment o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });

                for (Comment comment : sortedComments) {
                    if (!hasComment(comment, result.getComments())) {
                        responseParser.parseTfsCreateUpdateWorkItemFieldsResponseJson(
                                EntityUtils.toString(httpClient.patch(updateTaskUrl,
                                        TfsRequestBuilder.buildUpdateCommentBody(comment),
                                        new BasicNameValuePair("Content-Type", "application/json-patch+json")).getEntity()));
                    }
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
        return null;
    }

    /**
     * Method that will be called if task must be created
     * @param task
     * @throws Exception
     * @return changed task
     */
    private Task createTask(Task task) throws Exception {
        ApacheHttpClient httpClient = new ApacheHttpClient(createHttpClient(task));

        String url = task.getTargetUrl();

        String createTaskUrl = TfsRequestBuilder.buildCreateUrl(url);
        String createRequestBody = TfsRequestBuilder.buildCreateRequestBody(formFieldValuePairs(task, false));
        try {
            CloseableHttpResponse httpResponse = httpClient.patch(createTaskUrl, createRequestBody,
                    new BasicNameValuePair("Content-Type", "application/json-patch+json"));
            if (httpResponse.getStatusLine().getStatusCode() == 401)
                throw new AuthException("An error occured during Task creation. The credentials provided were not" +
                        "authorized to modify the task.");

            if (httpResponse.getStatusLine().getStatusCode() == 404)
                throw new InvalidInputDataException("An error occured during Task creation. The project target url was" +
                        "incorrect.");

            if (httpResponse.getStatusLine().getStatusCode() == 400)
                throw new InternalServerException("An error occured during Task creation.");
            String response = EntityUtils.toString(httpResponse.getEntity());
            logger.info(response);
            Task result = responseParser.parseTfsCreateUpdateWorkItemFieldsResponseJson(response);

            if (task.getComments() != null && task.getComments().size() > 0) {
                String updateTaskUrl = TfsRequestBuilder.buildUpdateUrl(url, result.getTaskId());
                for (Comment comment : task.getComments()) {
                    result = responseParser.parseTfsCreateUpdateWorkItemFieldsResponseJson(
                            EntityUtils.toString(httpClient.patch(updateTaskUrl,
                                    TfsRequestBuilder.buildUpdateCommentBody(comment),
                                    new BasicNameValuePair("Content-Type", "application/json-patch+json")).getEntity()));
                }
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }   finally {
            httpClient.close();
        }

        return null;
    }

    private CloseableHttpClient createHttpClient(Task task) {
        HttpClientBuilder builder = HttpClientBuilder.create();

        String login = task.getAuth().getLogin();
        String password = task.getAuth().getPassword();
        if (login != null && !login.isEmpty() && password != null) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new NTCredentials(task.getAuth().getLogin(), task.getAuth().getPassword(),
                            task.getAuth().getWorkstation(), task.getAuth().getDomain()));
            builder.setDefaultCredentialsProvider(credentialsProvider);
        }

        return builder.build();
    }

    List<FieldValuePair> formFieldValuePairs(Task task, boolean update) {
        List<FieldValuePair> pairs = new ArrayList<>();
        String name = task.getName();
        if (name != null && !name.isEmpty()) {
            pairs.add(new FieldValuePair(TfsRequestBuilder.TASK_NAME, name, update));
        }

        Task.Status state = task.getStatus();
        pairs.add(new FieldValuePair(TfsRequestBuilder.TASK_STATE, TfsRequestBuilder.getTfsTaskState(state), update));

        String description = task.getDescription();
        if (description != null && !description.isEmpty()) {
            pairs.add(new FieldValuePair(TfsRequestBuilder.TASK_DESCR, description, update));
        }

        String username = task.getUsername();
        String email = task.getUserEmail();
        if (username != null && !username.isEmpty() &&
                email != null && !email.isEmpty())
            pairs.add(new FieldValuePair(update ? TfsRequestBuilder.CHANGED_BY : TfsRequestBuilder.CREATED_BY,
                    username + " <" + email + ">", update));

        String assigneeName = task.getAssigneeUsername();
        String assigneeEmail = task.getAssigneeEmail();
        if (assigneeName != null && !assigneeName.isEmpty() &&
                assigneeEmail != null && !assigneeEmail.isEmpty())
            pairs.add(new FieldValuePair(TfsRequestBuilder.TASK_ASSIGNEE,
                    assigneeName + " <" + assigneeEmail + ">", update));
        return pairs;
    }

    private boolean hasComment(Comment comment, Collection<Comment> comments) {
        for (Comment comment1 : comments) {
            if (comment.getText().equals(comment1.getText()) && comment.getUsername().equals(comment1.getUsername()))
                return true;
        }
        return false;
    }
}
