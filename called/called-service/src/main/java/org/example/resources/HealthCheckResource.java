package org.example.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/health")
public class HealthCheckResource  {

    @GET
    public Response healthCheck() {
        return Response.ok("OK").build();
    }
}
