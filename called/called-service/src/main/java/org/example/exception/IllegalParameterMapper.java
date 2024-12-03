package org.example.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.library.dto.response.ApiErrorResponse;
import org.library.exception.IllegalParameterException;

@Provider
public class IllegalParameterMapper implements ExceptionMapper<IllegalParameterException> {
    @Override
    public Response toResponse(IllegalParameterException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(
                        new ApiErrorResponse(
                                "Illegal parameter",
                                "400",
                                "IllegalParameterException",
                                exception.getMessage()
                                )
                )
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
