package org.example.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.library.dto.response.ResourceNotFoundErrorResponse;
import org.library.exception.ResourceNotFoundException;

import java.time.LocalDateTime;

@Provider
public class ResourceNotFoundMapper implements ExceptionMapper<ResourceNotFoundException> {
    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new ResourceNotFoundErrorResponse(
                        "404",
                        exception.getMessage(),
                        LocalDateTime.now().toString()
                        )
                )
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
