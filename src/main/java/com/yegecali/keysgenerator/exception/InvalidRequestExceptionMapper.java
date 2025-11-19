package com.yegecali.keysgenerator.exception;

import com.yegecali.keysgenerator.dto.ErrorResponse;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class InvalidRequestExceptionMapper implements ExceptionMapper<InvalidRequestException> {
    @Override
    public Response toResponse(InvalidRequestException exception) {
        ErrorResponse err = new ErrorResponse("invalid_request", exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
    }
}

