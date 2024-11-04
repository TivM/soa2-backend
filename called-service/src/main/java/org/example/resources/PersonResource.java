package org.example.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.library.dto.Coordinates;
import org.library.dto.Location;
import org.library.dto.Page;
import org.library.dto.request.PersonRequest;
import org.library.dto.response.*;
import org.library.entity.Person;
import org.example.service.impl.PersonServiceImpl;
import org.library.exception.IllegalParameterException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/persons")
public class PersonResource {

    private final PersonServiceImpl personService;

    public PersonResource() {
        personService = new PersonServiceImpl();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerson(PersonRequest personRequest) {
        Person person = personService.add(
                personRequest.name(),
                personRequest.coordinates(),
                personRequest.height(),
                personRequest.birthday(),
                personRequest.passportID(),
                personRequest.hairColor(),
                personRequest.eyesColor(),
                personRequest.nationality(),
                personRequest.location()
        );
        return Response.ok(createPersonResponse(person)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons(final HttpServletRequest request) {
//        List<Person> persons = personService.getAll();
//        List<PersonResponse> listPersonResponses = new ArrayList<>();
//        for (var person: persons) {
//            listPersonResponses.add(
//                    createPersonResponse(person)
//            );
//        }
        String[] sortParameters = request.getParameterValues("sort");
        String[] filterParameters = request.getParameterValues("filter");

        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");
        Integer page = null, pageSize = null;

        if (!pageParam.isEmpty()) {
            page = Integer.parseInt(pageParam);
            if (page <= 0) {
                throw new IllegalParameterException("wrong page param");
            }
        }
        if (!pageSizeParam.isEmpty()) {
            pageSize = Integer.parseInt(pageSizeParam);
            if (pageSize <= 0) {
                throw new IllegalParameterException("wrong page size");
            }
        }


        List<String> sort = sortParameters == null
                ? new ArrayList<>()
                : Stream.of(sortParameters).filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList());
        List<String> filter = filterParameters == null
                ? new ArrayList<>()
                : Stream.of(filterParameters).filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList());

        Page<Person> resultPage = personService.getPersonsFilter(sort, filter, page, pageSize);

        if (resultPage == null) {
            return Response.ok(new ListPersonResponse(List.of(), 0, 0 ,0 ,0L)).build();
        }

        List<PersonResponse> listPersonResponses = new ArrayList<>();

        for (var person: resultPage.objects()) {
            listPersonResponses.add(
                    createPersonResponse(person)
            );
        }

        return Response.ok(new ListPersonResponse(
                listPersonResponses,
                resultPage.page(),
                resultPage.pageSize(),
                resultPage.totalPages(),
                resultPage.totalCount()
                )
        ).build();
    }

    @GET
    @Path("/{person_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonById(@PathParam("person_id") int personId) {
        Person person = personService.getByPersonId(personId).get();
        return Response.ok(createPersonResponse(person)).build();
    }

    @GET
    @Path("/height/{operation}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonHeight(@PathParam("operation") String operation) {
        double height = personService.getHeight(operation);
        return switch (operation.toUpperCase()) {
            case "AVERAGE" -> Response.ok(new AvgHeightResponse(height)).build();
            case "MAX" -> Response.ok(new MaxHeightResponse(height)).build();
            case "MIN" -> Response.ok(new MinHeightResponse(height)).build();
            default -> throw new RuntimeException("Operation doesn't exist");
        };
    }

    @GET
    @Path("/enum/{enumName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonById(@PathParam("enumName") String enumName) {
        List<String> enumValues = personService.getPersonEnum(enumName);
        return Response.ok(new EnumValuesResponse(enumValues)).build();
    }

    @DELETE
    @Path("/{person_id}")
    public Response deletePersonById(@PathParam("person_id") int personId) {
        personService.deleteByPersonId(personId);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{person_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePersonById(@PathParam("person_id") int personId, PersonRequest personRequest) {
        Person person = personService.updateByPersonId(
                personId,
                personRequest.name(),
                personRequest.coordinates(),
                personRequest.height(),
                personRequest.birthday(),
                personRequest.passportID(),
                personRequest.hairColor(),
                personRequest.eyesColor(),
                personRequest.nationality(),
                personRequest.location());
        return Response.ok(createPersonResponse(person)).build();
    }

    private PersonResponse createPersonResponse(Person person){
        return new PersonResponse(
                person.getId(),
                person.getName(),
                new Coordinates(person.getCoordinateX(), person.getCoordinateY()),
                person.getCreationDate(),
                person.getHeight(),
                person.getBirthday(),
                person.getPassportID(),
                person.getHairColor(),
                person.getEyesColor(),
                person.getNationality(),
                new Location(person.getLocationCoordinateX(), person.getLocationCoordinateY(), person.getLocationName())
        );
    }
}
