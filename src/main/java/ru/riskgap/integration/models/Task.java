package ru.riskgap.integration.models;

/**
 * Created by Nikita on 16.06.2015.
 */
public class Task {

    /**
     * Название задачи
     */
    public static final String TASK_NAME = "name";
    private String name;

    /**
     * Статус задачи
     */
    public static final String TASK_STATUS = "status";
    private String status; //todo use enum?

    /**
     * Описание задачи
     */
    public static final String TASK_DESCRIPTION = "description";
    private String description;

    /**
     * Идентификатор пользователя, создавшего задачу (если известен)
     */
    public static final String USER_ID = "user-id";
    private String userId;

    /**
     * Имя пользователя, создавшего задачу
     */
    public static final String USERNAME = "username";
    private String username;

    /**
     * E-mail пользователя, создавшего задачу
     */
    public static final String USER_EMAIL = "user-email";
    private String userEmail;

    /**
     * Идентификатор пользователя, назначенного на задачу
     */
    public static final String ASSIGNEE_ID = "assignee-id";
    private String assigneeId;

    /**
     * Имя пользователя, назначенного на задачу (если известен)
     */
    public static final String ASSIGNEE_USERNAME = "assignee-username";
    private String assigneeUsername;

    /**
     * E-mail пользователя, назначенного на задачу
     */
    public static final String ASSIGNEE_EMAIL = "assignee-email";
    private String assigneeEmail;

    /**
     * Дата исполнения задачи
     */
    public static final String TASK_DUE = "due";
    private String due; //todo use java.util.Date?

    /**
     * Ссылка на риск
     */
    public static final String RISK_REF = "risk-reference";
    private String riskRef; //todo format?

    /**
     * Система, в которой находится задание
     */
    public static final String TARGET_SYSTEM = "target-system";
    private TargetSystemEnum targetSystem;


    public Task() {
        //no op todo Builder pattern?
    }

    //Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getRiskRef() {
        return riskRef;
    }

    public void setRiskRef(String riskRef) {
        this.riskRef = riskRef;
    }

    public TargetSystemEnum getTargetSystem() {
        return targetSystem;
    }

    public void setTargetSystem(TargetSystemEnum targetSystem) {
        this.targetSystem = targetSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (assigneeEmail != null ? !assigneeEmail.equals(task.assigneeEmail) : task.assigneeEmail != null)
            return false;
        if (assigneeId != null ? !assigneeId.equals(task.assigneeId) : task.assigneeId != null) return false;
        if (assigneeUsername != null ? !assigneeUsername.equals(task.assigneeUsername) : task.assigneeUsername != null)
            return false;
        if (description != null ? !description.equals(task.description) : task.description != null) return false;
        if (due != null ? !due.equals(task.due) : task.due != null) return false;
        if (name != null ? !name.equals(task.name) : task.name != null) return false;
        if (riskRef != null ? !riskRef.equals(task.riskRef) : task.riskRef != null) return false;
        if (status != null ? !status.equals(task.status) : task.status != null) return false;
        if (targetSystem != null ? !targetSystem.equals(task.targetSystem) : task.targetSystem != null) return false;
        if (userEmail != null ? !userEmail.equals(task.userEmail) : task.userEmail != null) return false;
        if (userId != null ? !userId.equals(task.userId) : task.userId != null) return false;
        if (username != null ? !username.equals(task.username) : task.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
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
        result = 31 * result + (targetSystem != null ? targetSystem.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", assigneeId='" + assigneeId + '\'' +
                ", assigneeUsername='" + assigneeUsername + '\'' +
                ", assigneeEmail='" + assigneeEmail + '\'' +
                ", due='" + due + '\'' +
                ", riskRef='" + riskRef + '\'' +
                ", targetSystem=" + targetSystem +
                '}';
    }

    public String toJson() {
        return "{" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"status\": \"" + status + "\",\n" +
                "  \"description\": \"" + description + "\",\n" +
                "  \"user-id\": \"" + userId + "\",\n" +
                "  \"username\": \"" + username + "\",\n" +
                "  \"user-email\": \"" + userEmail + "\",\n" +
                "  \"assignee-id\": \"" + assigneeId + "\",\n" +
                "  \"assignee-username\": \"" + assigneeUsername + "\",\n" +
                "  \"assignee-email\": \"" + assigneeEmail + "\",\n" +
                "  \"due\": \"" + due + "\",\n" +
                "  \"risk-reference\": \"" + riskRef + "\",\n" +
                "  \"target-system\": \"" + targetSystem + "\"\n" +
                "}";
    }
}
