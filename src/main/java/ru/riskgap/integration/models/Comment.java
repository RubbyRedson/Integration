package ru.riskgap.integration.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

/**
 * Created by Nikita on 16.06.2015.
 */
public class Comment {
    /*
 - список комментариев (дата, коментатор, текст комментария, ключ api, если комментарий уже существует в интегрируемой системе)
     */

    /**
     * ID комментария во внешней системе
     */
    @JsonProperty("comment-id")
    private String commentId;

    public static final String DATE = "date";
    @JsonProperty("date")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date date;

    /**
     * Имя пользователя, написавшего комментарий
     */
    public static final String USERNAME = "username";
    @JsonProperty("username")
    private String username;

    /**
     * E-mail пользователя, написавшего комментарий
     */
    public static final String EMAIL = "email";
    @JsonProperty("email")
    private String email;

    /**
     * ID пользователя, написавшего комментарий
     */
    @JsonProperty("user-id")
    private String userId;

    /**
     * Текст комментария
     */
    public static final String TEXT = "text";
    @JsonProperty("text")
    private String text;

    /**
     * Ключ в API
     */
    public static final String API_KEY = "api-key";
    @JsonProperty("api-key")
    private String key;

    public Comment() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (commentId != null ? !commentId.equals(comment.commentId) : comment.commentId != null) return false;
        if (date != null ? !date.equals(comment.date) : comment.date != null) return false;
        if (username != null ? !username.equals(comment.username) : comment.username != null) return false;
        if (email != null ? !email.equals(comment.email) : comment.email != null) return false;
        if (userId != null ? !userId.equals(comment.userId) : comment.userId != null) return false;
        if (text != null ? !text.equals(comment.text) : comment.text != null) return false;
        return !(key != null ? !key.equals(comment.key) : comment.key != null);

    }

    @Override
    public int hashCode() {
        int result = commentId != null ? commentId.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", date=" + date +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                ", text='" + text + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
