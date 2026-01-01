package com.yegecali.keysgenerator.exception;

import jakarta.ws.rs.core.Response;

public enum ErrorCode {
    // Validation errors (4xx)
    INVALID_REQUEST("invalid_request", Response.Status.BAD_REQUEST),
    UNKNOWN_TYPE("unknown_type", Response.Status.BAD_REQUEST),
    INVALID_KEY_SIZE("invalid_key_size", Response.Status.BAD_REQUEST),
    MISSING_PARAMETER("missing_parameter", Response.Status.BAD_REQUEST),
    UNSUPPORTED_ALGORITHM("unsupported_algorithm", Response.Status.BAD_REQUEST),

    // Key operations (5xx)
    KEY_GENERATION_FAILED("key_generation_failed", Response.Status.INTERNAL_SERVER_ERROR),
    KEY_PARSING_FAILED("key_parsing_failed", Response.Status.INTERNAL_SERVER_ERROR),
    MISSING_KEY("missing_key", Response.Status.BAD_REQUEST),

    // Encryption/Decryption (5xx)
    ENCRYPTION_FAILED("encryption_failed", Response.Status.INTERNAL_SERVER_ERROR),
    DECRYPTION_FAILED("decryption_failed", Response.Status.INTERNAL_SERVER_ERROR),
    INVALID_CIPHERTEXT("invalid_ciphertext", Response.Status.BAD_REQUEST),

    // System errors (5xx)
    INTERNAL_ERROR("internal_error", Response.Status.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("service_unavailable", Response.Status.SERVICE_UNAVAILABLE);

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

