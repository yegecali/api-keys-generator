package com.yegecali.keysgenerator.exception;

public class InvalidRequestException extends ApplicationException {
    public InvalidRequestException(String message) { super(ErrorCode.INVALID_REQUEST, message); }
}
