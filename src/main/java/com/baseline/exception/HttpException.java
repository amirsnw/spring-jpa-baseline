package com.baseline.exception;

import com.baseline.common.enumeration.HttpStatus;

import java.util.List;

public class HttpException extends RuntimeException {
    private String[] parameters;
    private HttpStatus status;
    private List<Object> detail;
    private boolean sendAlertToAdmin;

    public HttpException() {
        this.sendAlertToAdmin = false;
    }

    public HttpException(String message) {
        super(message);
        this.sendAlertToAdmin = false;
    }

    public HttpException(String message, List<Object> detail) {
        super(message);
        this.sendAlertToAdmin = false;
        this.setDetail(detail);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
        this.sendAlertToAdmin = false;
    }

    public HttpException(Throwable cause) {
        super(cause);
        this.sendAlertToAdmin = false;
    }

    public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.sendAlertToAdmin = false;
    }

    public HttpException(String message, String... parameters) {
        super(message);
        this.sendAlertToAdmin = false;
        this.parameters = parameters;
    }

    public HttpException(String message, Throwable cause, String... parameters) {
        super(message, cause);
        this.sendAlertToAdmin = false;
        this.parameters = parameters;
    }

    public HttpException(String message, HttpStatus status, String[] parameters) {
        this(message, status);
        this.parameters = parameters;
    }

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.sendAlertToAdmin = false;
        this.status = status;
    }

    public HttpException(String message, int status) {
        super(message);
        this.sendAlertToAdmin = false;
        this.status = HttpStatus.valueOf(status);
    }

    public String[] getParameters() {
        return this.parameters;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public List<Object> getDetail() {
        return this.detail;
    }

    public void setDetail(List<Object> detail) {
        this.detail = detail;
    }

}