package ru.riskgap.integration.models;

import java.util.Date;

/**
 * Created by andrey on 12.07.15.
 */
public class CommentBuilder {
    private Comment comment;

    public CommentBuilder() {
        comment = new Comment();
    }

    public CommentBuilder setDate(Date date) {
        comment.setDate(date);
        return this;
    }


    public CommentBuilder setUsername(String username) {
        comment.setUsername(username);
        return this;
    }



    public CommentBuilder setText(String text) {
        comment.setText(text);
        return this;
    }


    public CommentBuilder setKey(String key) {
        comment.setKey(key);
        return this;
    }

    public CommentBuilder setUserId(String userId) {
        comment.setUserId(userId);
        return this;
    }

    public CommentBuilder setCommentId(String commentId) {
        comment.setCommentId(commentId);
        return this;
    }

    public Comment build() {
        return comment;
    }
}
