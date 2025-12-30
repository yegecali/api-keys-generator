package com.yegecali.keysgenerator.exception;

import jakarta.ws.rs.core.Response;

public enum ErrorCode {
    INVALID_REQUEST("invalid_request", Response.Status.BAD_REQUEST),
    UNKNOWN_TYPE("unknown_type", Response.Status.BAD_REQUEST),
    KEY_GENERATION_FAILED("key_generation_failed", Response.Status.INTERNAL_SERVER_ERROR),
    INTERNAL_ERROR("internal_error", Response.Status.INTERNAL_SERVER_ERROR);

    private final String code;
    private final Response.Status defaultStatus;

    ErrorCode(String code, Response.Status defaultStatus) {
        this.code = code;
        this.defaultStatus = defaultStatus;
    }

    public String getCode() {
        return code;
    }

    public int getDefaultStatus() {
        return defaultStatus.getStatusCode();
    }
}

