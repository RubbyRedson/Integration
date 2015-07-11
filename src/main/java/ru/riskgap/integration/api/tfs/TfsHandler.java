package ru.riskgap.integration.api.tfs;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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
                    httpClient.extractEntity(httpClient.get(getFieldsUrl), false));
            List<Comment> commentList = responseParser.parseTfsGetWorkItemHistoryResponseJson(
                    httpClient.extractEntity(httpClient.get(getCommentsUrl), true));
            //todo set comments in task
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
            httpClient.patch(updateTaskUrl, updateRequestBody);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
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
        }   finally {
            httpClient.close();
        }

        return getTaskInformation(task);
    }

    private CloseableHttpClient createHttpClient(Task task) {
        HttpClientBuilder builder = HttpClientBuilder.create();

        String login = task.getAuth().getLogin();
        String password = task.getAuth().getPassword();
        if (login != null && !login.isEmpty() && password != null) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(task.getAuth().getLogin(), task.getAuth().getPassword()));
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
        pairs.add(new FieldValuePair(TfsRequestBuilder.TASK_STATE, state.getStatus(), update));

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
}
