package com.yegecali.keysgenerator.exception;

public class KeyGenerationException extends ApplicationException {
    public KeyGenerationException(String message) {
        super(ErrorCode.KEY_GENERATION_FAILED, message);
    }

    public KeyGenerationException(String message, Throwable cause) {
        super(ErrorCode.KEY_GENERATION_FAILED, message, ErrorCode.KEY_GENERATION_FAILED.getDefaultStatus(), null, cause);
    }
}
