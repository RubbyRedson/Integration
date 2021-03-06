package ru.riskgap.integration.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.riskgap.integration.exceptions.InvalidInputDataException;

import java.util.Date;
import java.util.List;

/**
 * Created by Nikita on 16.06.2015.
 * Edited by andrey on 05.07.2015
 */
public class Task {

    /**
     * Идентификатор контейнера, в котором находится задача
     * (напр., проект или доска)
     */
    @JsonProperty("container-id")
    private String containerId;

    /**
     * Идентификатор задачи
     */
    @JsonProperty("task-id")
    private String taskId;

    /**
     * Название задачи
     */
    public static final String TASK_NAME = "name";

    @JsonProperty("name")
    private String name;

    /**
     * Статус задачи
     */
    public static final String TASK_STATUS = "status";
    @JsonProperty("status")
    private Status status;

    /**
     * Описание задачи
     */
    public static final String TASK_DESCRIPTION = "description";
    @JsonProperty("description")
    private String description;

    /**
     * Идентификатор пользователя, создавшего задачу (если известен)
     */
    public static final String USER_ID = "user-id";
    @JsonProperty("user-id")
    private String userId;

    /**
     * Имя пользователя, создавшего задачу
     */
    public static final String USERNAME = "username";
    @JsonProperty("username")
    private String username;

    /**
     * E-mail пользователя, создавшего задачу
     */
    public static final String USER_EMAIL = "user-email";
    @JsonProperty("user-email")
    private String userEmail;

    /**
     * Идентификатор пользователя, назначенного на задачу
     */
    public static final String ASSIGNEE_ID = "assignee-id";
    @JsonProperty("assignee-id")
    private String assigneeId;

    /**
     * Имя пользователя, назначенного на задачу (если известен)
     */
    public static final String ASSIGNEE_USERNAME = "assignee-username";
    @JsonProperty("assignee-username")
    private String assigneeUsername;

    /**
     * E-mail пользователя, назначенного на задачу
     */
    public static final String ASSIGNEE_EMAIL = "assignee-email";
    @JsonProperty("assignee-email")
    private String assigneeEmail;

    /**
     * Дата исполнения задачи
     */
    public static final String TASK_DUE = "due";

    @JsonProperty("due")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date due;

    /**
     * Ссылка на риск
     */
    public static final String RISK_REF = "risk-reference";
    @JsonProperty("risk-reference")
    private String riskRef;



    /**
     * Конечный адрес задачи в интегрируемой системе
     */
    @JsonProperty("target-url")
    private String targetUrl;


    @JsonProperty("comments")
    private List<Comment> comments;


    private Auth auth;

    /*
    todo TFS
    * need full url to collection or url to tfs + collection name
    * need project name (?)
    * need task id or not guaranties of finding it
     */


    public Task() {
        //no op
    }

    //Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeUsername() {
        return assigneeUsername;
    }

    public void setAssigneeUsername(String assigneeUsername) {
        this.assigneeUsername = assigneeUsername;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public String getRiskRef() {
        return riskRef;
    }

    public void setRiskRef(String riskRef) {
        this.riskRef = riskRef;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @JsonIgnore
    public Auth getAuth() {
        return auth;
    }

    @JsonProperty("auth")
    public void setAuth(Auth auth) {
        this.auth = auth;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (containerId != null ? !containerId.equals(task.containerId) : task.containerId != null) return false;
        if (taskId != null ? !taskId.equals(task.taskId) : task.taskId != null) return false;
        if (name != null ? !name.equals(task.name) : task.name != null) return false;
        if (status != task.status) return false;
        if (description != null ? !description.equals(task.description) : task.description != null) return false;
        if (userId != null ? !userId.equals(task.userId) : task.userId != null) return false;
        if (username != null ? !username.equals(task.username) : task.username != null) return false;
        if (userEmail != null ? !userEmail.equals(task.userEmail) : task.userEmail != null) return false;
        if (assigneeId != null ? !assigneeId.equals(task.assigneeId) : task.assigneeId != null) return false;
        if (assigneeUsername != null ? !assigneeUsername.equals(task.assigneeUsername) : task.assigneeUsername != null)
            return false;
        if (assigneeEmail != null ? !assigneeEmail.equals(task.assigneeEmail) : task.assigneeEmail != null)
            return false;
        if (due != null ? !due.equals(task.due) : task.due != null) return false;
        if (riskRef != null ? !riskRef.equals(task.riskRef) : task.riskRef != null) return false;
        if (targetUrl != null ? !targetUrl.equals(task.targetUrl) : task.targetUrl != null) return false;
        if (comments != null ? !comments.equals(task.comments) : task.comments != null) return false;
        return !(auth != null ? !auth.equals(task.auth) : task.auth != null);

    }

    @Override
    public int hashCode() {
        int result = containerId != null ? containerId.hashCode() : 0;
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (assigneeId != null ? assigneeId.hashCode() : 0);
        result = 31 * result + (assigneeUsername != null ? assigneeUsername.hashCode() : 0);
        result = 31 * result + (assigneeEmail != null ? assigneeEmail.hashCode() : 0);
        result = 31 * result + (due != null ? due.hashCode() : 0);
        result = 31 * result + (riskRef != null ? riskRef.hashCode() : 0);
        result = 31 * result + (targetUrl != null ? targetUrl.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (auth != null ? auth.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "containerId='" + containerId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", assigneeId='" + assigneeId + '\'' +
                ", assigneeUsername='" + assigneeUsername + '\'' +
                ", assigneeEmail='" + assigneeEmail + '\'' +
                ", due=" + due +
                ", riskRef='" + riskRef + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", comments=" + comments +
                ", auth=" + auth +
                '}';
    }


    /**
     * Статус задачи
     */
    public enum Status {
        OPEN("open"),
        IN_PROGRESS("in progress"),
        RESOLVED("resolved"),
        CLOSED("closed"),
        UNKNOWN(null);

        private String status;

        Status(String status) {
            this.status = status;
        }

        @JsonValue
        public String getStatus() {
            return status;
        }

        @JsonCreator
        public static Status fromString(String status) throws InvalidInputDataException {
            if (status != null) {
                for (Status enumStatus : Status.values()) {
                    if (status.equals(enumStatus.getStatus()))
                        return enumStatus;
                }
            }
            return UNKNOWN;
        }
    }


}
