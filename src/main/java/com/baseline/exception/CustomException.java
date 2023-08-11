package com.baseline.exception;

import java.util.List;

public class CustomException extends RuntimeException {
    private List<Object> detail;

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, List<Object> detail) {
        super(message);
        this.setDetail(detail);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CustomException(String message, String... parameters) {
        super(message);
    }

    public CustomException(String message, Throwable cause, String... parameters) {
        super(message, cause);
    }

    public List<Object> getDetail() {
        return this.detail;
    }

    public void setDetail(List<Object> detail) {
        this.detail = detail;
    }

}