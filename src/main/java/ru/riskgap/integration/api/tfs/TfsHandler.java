package ru.riskgap.integration.api.tfs;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ru.riskgap.integration.IntegrationHandler;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.ApacheHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TfsHandler implements IntegrationHandler {

    @Autowired
    TfsResponseParser responseParser;

    @Override
    public Task createOrUpdateTask(Task task) {
        //TODO implement method
        if (true) { //update
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
            Task result = responseParser.parseTfsGetWorkItemFieldsResponseJson(
                    EntityUtils.toString(httpClient.get(getFieldsUrl).getEntity()));
            List<Comment> commentList = responseParser.parseTfsGetWorkItemHistoryResponseJson(
                    EntityUtils.toString(httpClient.get(getCommentsUrl).getEntity()));
            //todo set comments in task
            return result;
        } catch (IOException e) {
            e.printStackTrace();
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
            httpClient.patch(updateTaskUrl, updateRequestBody).getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getTaskInformation(task);
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
            httpClient.patch(createTaskUrl, createRequestBody).getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getTaskInformation(task);
    }

    private CloseableHttpClient createHttpClient(Task task) {
        HttpClientBuilder builder = HttpClientBuilder.create();

        String login = task.getLogin();
        String password = task.getPassword();
        if (login != null && !login.isEmpty() && password != null) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(task.getLogin(), task.getPassword()));
            builder.setDefaultCredentialsProvider(credentialsProvider);
        }

        return builder.build();
    }

    private List<FieldValuePair> formFieldValuePairs(Task task, boolean update) {
        List<FieldValuePair> pairs = new ArrayList<>();
        String name = task.getName();
        if (name != null && !name.isEmpty()) {
            pairs.add(new FieldValuePair(TfsRequestBuilder.TASK_NAME, name, update));
        }

        Task.Status state = task.getStatus();
        pairs.add(new FieldValuePair(TfsRequestBuilder.TASK_STATE, state.getStatus(), update));

        String description = task.getDescription();
        if (description != null && !description.isEmpty()) {
            pairs.add(new FieldValuePair(TfsRequestBuilder.TASK_DESCR, description, update));
        }

        String username = task.getUsername();
        String email = task.getUserEmail();
        if (username != null && !username.isEmpty() &&
                email != null && !email.isEmpty())
            pairs.add(new FieldValuePair(username + " <" + email + ">",
                    update ? TfsRequestBuilder.CHANGED_BY : TfsRequestBuilder.CREATED_BY, update));

        String assigneeName = task.getAssigneeUsername();
        String assigneeEmail = task.getAssigneeEmail();
        if (assigneeName != null && !assigneeName.isEmpty() &&
                assigneeEmail != null && !assigneeEmail.isEmpty())
            pairs.add(new FieldValuePair(assigneeName + " <" + assigneeEmail + ">",
                    TfsRequestBuilder.TASK_ASSIGNEE, update));
        return pairs;
    }
}
