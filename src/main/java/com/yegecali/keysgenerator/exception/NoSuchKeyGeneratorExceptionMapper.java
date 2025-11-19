package com.yegecali.keysgenerator.exception;

import com.yegecali.keysgenerator.dto.ErrorResponse;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class NoSuchKeyGeneratorExceptionMapper implements ExceptionMapper<NoSuchKeyGeneratorException> {
    @Override
    public Response toResponse(NoSuchKeyGeneratorException exception) {
        ErrorResponse err = new ErrorResponse("unknown_type", exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
    }
}

