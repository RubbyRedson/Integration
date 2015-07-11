package ru.riskgap.integration.models;

import java.util.Date;
import java.util.List;

/**
 * Created by andrey on 11.07.15.
 */
public class TaskBuilder {
    private Task task;

    public TaskBuilder() {
        task = new Task();
    }

    public TaskBuilder setName(String name) {
        task.setName(name);
        return this;
    }

    public TaskBuilder setStatus(Task.Status status) {
        task.setStatus(status);
        return this;
    }


    public TaskBuilder setDescription(String description) {
        task.setDescription(description);
        return this;
    }


    public TaskBuilder setUserId(String userId) {
        task.setUserId(userId);
        return this;
    }


    public TaskBuilder setUsername(String username) {
        task.setUsername(username);
        return this;
    }


    public TaskBuilder setUserEmail(String userEmail) {
        task.setUserEmail(userEmail);
        return this;
    }

    public TaskBuilder setAssigneeId(String assigneeId) {
        task.setAssigneeId(assigneeId);
        return this;
    }


    public TaskBuilder setAssigneeUsername(String assigneeUsername) {
        task.setAssigneeUsername(assigneeUsername);
        return this;
    }


    public TaskBuilder setAssigneeEmail(String assigneeEmail) {
        task.setAssigneeEmail(assigneeEmail);
        return this;
    }


    public TaskBuilder setDue(Date due) {
        task.setDue(due);
        return this;
    }


    public TaskBuilder setRiskRef(String riskRef) {
        task.setRiskRef(riskRef);
        return this;
    }


    public TaskBuilder setTaskId(String taskId) {
        task.setTaskId(taskId);
        return this;
    }


    public TaskBuilder setContainerId(String containerId) {
        task.setContainerId(containerId);
        return this;
    }


    public TaskBuilder setTargetUrl(String targetUrl) {
        task.setTargetUrl(targetUrl);
        return this;
    }


    public TaskBuilder setComments(List<Comment> comments) {
        task.setComments(comments);
        return this;
    }

    public TaskBuilder setAuth(Auth auth) {
        task.setAuth(auth);
        return this;
    }

    public Task build() {
        return task;
    }
}
