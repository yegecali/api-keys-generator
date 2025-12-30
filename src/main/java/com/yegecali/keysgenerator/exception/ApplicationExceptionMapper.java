package com.yegecali.keysgenerator.exception;

import com.yegecali.keysgenerator.openapi.model.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {
    @Override
    public Response toResponse(ApplicationException exception) {
        ErrorResponse err = new ErrorResponse();
        err.setError(exception.getErrorCode().getCode());
        err.setMessage(exception.getMessage());
        return Response.status(exception.getStatus()).entity(err).build();
    }
}
