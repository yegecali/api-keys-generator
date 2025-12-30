package com.yegecali.keysgenerator.exception;

public class NoSuchKeyGeneratorException extends ApplicationException {
    public NoSuchKeyGeneratorException(String message) { super(ErrorCode.UNKNOWN_TYPE, message); }
}
