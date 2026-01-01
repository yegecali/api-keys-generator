package com.yegecali.keysgenerator.exception;

import java.util.Collections;
import java.util.Map;

public class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int status;
    private final Map<String, String> metadata;

    // Constructor package-private - usar factory methods o builder
    ApplicationException(ErrorCode errorCode, String message, int status,
                        Map<String, String> metadata, Throwable cause) {
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

    // ============================================================================
    // FACTORY METHODS - Forma rápida de crear excepciones simples
    // ============================================================================

    // Validation errors
    public static ApplicationException invalidRequest(String message) {
        return new ApplicationException(ErrorCode.INVALID_REQUEST, message,
            ErrorCode.INVALID_REQUEST.getDefaultStatus(), null, null);
    }

    public static ApplicationException unknownType(String message) {
        return new ApplicationException(ErrorCode.UNKNOWN_TYPE, message,
            ErrorCode.UNKNOWN_TYPE.getDefaultStatus(), null, null);
    }

    public static ApplicationException invalidKeySize(String message) {
        return new ApplicationException(ErrorCode.INVALID_KEY_SIZE, message,
            ErrorCode.INVALID_KEY_SIZE.getDefaultStatus(), null, null);
    }

    public static ApplicationException missingParameter(String message) {
        return new ApplicationException(ErrorCode.MISSING_PARAMETER, message,
            ErrorCode.MISSING_PARAMETER.getDefaultStatus(), null, null);
    }

    public static ApplicationException unsupportedAlgorithm(String message) {
        return new ApplicationException(ErrorCode.UNSUPPORTED_ALGORITHM, message,
            ErrorCode.UNSUPPORTED_ALGORITHM.getDefaultStatus(), null, null);
    }

    // Key operations
    public static ApplicationException keyGenerationFailed(String message, Throwable cause) {
        return new ApplicationException(ErrorCode.KEY_GENERATION_FAILED, message,
            ErrorCode.KEY_GENERATION_FAILED.getDefaultStatus(), null, cause);
    }

    public static ApplicationException keyParsingFailed(String message, Throwable cause) {
        return new ApplicationException(ErrorCode.KEY_PARSING_FAILED, message,
            ErrorCode.KEY_PARSING_FAILED.getDefaultStatus(), null, cause);
    }

    public static ApplicationException missingKey(String message) {
        return new ApplicationException(ErrorCode.MISSING_KEY, message,
            ErrorCode.MISSING_KEY.getDefaultStatus(), null, null);
    }

    // Encryption/Decryption
    public static ApplicationException encryptionFailed(String message, Throwable cause) {
        return new ApplicationException(ErrorCode.ENCRYPTION_FAILED, message,
            ErrorCode.ENCRYPTION_FAILED.getDefaultStatus(), null, cause);
    }

    public static ApplicationException decryptionFailed(String message, Throwable cause) {
        return new ApplicationException(ErrorCode.DECRYPTION_FAILED, message,
            ErrorCode.DECRYPTION_FAILED.getDefaultStatus(), null, cause);
    }

    public static ApplicationException invalidCiphertext(String message) {
        return new ApplicationException(ErrorCode.INVALID_CIPHERTEXT, message,
            ErrorCode.INVALID_CIPHERTEXT.getDefaultStatus(), null, null);
    }

    // System errors
    public static ApplicationException internalError(String message, Throwable cause) {
        return new ApplicationException(ErrorCode.INTERNAL_ERROR, message,
            ErrorCode.INTERNAL_ERROR.getDefaultStatus(), null, cause);
    }

    public static ApplicationException serviceUnavailable(String message) {
        return new ApplicationException(ErrorCode.SERVICE_UNAVAILABLE, message,
            ErrorCode.SERVICE_UNAVAILABLE.getDefaultStatus(), null, null);
    }
}

