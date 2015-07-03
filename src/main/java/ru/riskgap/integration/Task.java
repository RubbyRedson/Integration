package ru.riskgap.integration;

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
     * Имя пользователя, создавшего задачу
     */
    public static final String ASSIGNEE_ID = "assignee-id";
    private String assigneeId;

    /**
     * Идентификатор пользователя, назначенного на задачу (если известен)
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
}
