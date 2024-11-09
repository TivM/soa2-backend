package org.example.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @GET
    public Response getPersonById() {
        // Бизнес-логика для получения человека по идентификатору
        // Заглушка для примера
        return Response.ok("aaa").build();
    }
}
