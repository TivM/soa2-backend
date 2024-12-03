package org.example.exception;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.library.dto.response.ApiErrorResponse;

@Provider
public class NumberFormatExceptionMapper implements ExceptionMapper<ProcessingException> {

    @Override
    public Response toResponse(ProcessingException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(
                        new ApiErrorResponse(
                                "Illegal parameter",
                                "400",
                                "IllegalParameterException",
                                "Wrong parameter in request"
                        )
                )
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
