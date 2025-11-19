package com.yegecali.keysgenerator.exception;

import com.yegecali.keysgenerator.dto.ErrorResponse;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        ErrorResponse err = new ErrorResponse("internal_error", "Unexpected error");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(err).build();
    }
}

