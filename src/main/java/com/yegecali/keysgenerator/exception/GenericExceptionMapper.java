package com.yegecali.keysgenerator.exception;

import com.yegecali.keysgenerator.openapi.model.ErrorResponse;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        ErrorResponse err = new ErrorResponse();
        err.setError(ErrorCode.INTERNAL_ERROR.getCode());
        err.setMessage("Unexpected error");
        return Response.status(ErrorCode.INTERNAL_ERROR.getDefaultStatus()).entity(err).build();
    }
}
