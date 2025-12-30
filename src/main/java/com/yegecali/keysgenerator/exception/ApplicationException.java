package com.yegecali.keysgenerator.exception;

import java.util.Collections;
import java.util.Map;

public class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int status;
    private final Map<String, String> metadata;

    public ApplicationException(ErrorCode errorCode, String message) {
        this(errorCode, message, errorCode.getDefaultStatus(), Collections.emptyMap(), null);
    }

    public ApplicationException(ErrorCode errorCode, String message, int status) {
        this(errorCode, message, status, Collections.emptyMap(), null);
    }

    public ApplicationException(ErrorCode errorCode, String message, Map<String, String> metadata) {
        this(errorCode, message, errorCode.getDefaultStatus(), metadata, null);
    }

    public ApplicationException(ErrorCode errorCode, String message, int status, Map<String, String> metadata, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.status = status;
        this.metadata = metadata == null ? Collections.emptyMap() : metadata;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    // Convenience factories
    public static ApplicationException invalidRequest(String message) {
        return new ApplicationException(ErrorCode.INVALID_REQUEST, message);
    }

    public static ApplicationException unknownType(String message) {
        return new ApplicationException(ErrorCode.UNKNOWN_TYPE, message);
    }

    public static ApplicationException keyGenerationFailed(String message, Throwable cause) {
        return new ApplicationException(ErrorCode.KEY_GENERATION_FAILED, message, ErrorCode.KEY_GENERATION_FAILED.getDefaultStatus(), null, cause);
    }

    public static ApplicationException internalError(String message, Throwable cause) {
        return new ApplicationException(ErrorCode.INTERNAL_ERROR, message, ErrorCode.INTERNAL_ERROR.getDefaultStatus(), null, cause);
    }
}

