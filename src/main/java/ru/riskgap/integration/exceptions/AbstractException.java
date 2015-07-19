package ru.riskgap.integration.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractException extends Exception {
    private String error;
    private String detail;
    private Integer status;
    private String json;
    private ObjectMapper objectMapper = new ObjectMapper();
    public AbstractException(String error, String detail, Integer status) {
        this.error = error;
        this.detail = detail;
        this.status = status;
    }

    @Override
    public String getMessage() {
        if (json == null) {
            json = objectMapper.createObjectNode()
                    .put("error", error)
                    .put("detail", detail)
                    .put("status", status)
                    .toString();
        }
        return json;
    }

    public String getError() {
        return error;
    }

    public String getDetail() {
        return detail;
    }

    public Integer getStatus() {
        return status;
    }
}
