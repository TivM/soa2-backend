package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.library.dto.Coordinates;
import org.library.dto.Location;
import org.library.dto.Page;
import org.library.dto.request.PersonRequest;
import org.library.dto.response.*;
import org.library.entity.Person;

import org.library.enums.Color;
import org.library.enums.Nationality;
import org.library.exception.IllegalParameterException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        if (isEnumCorrect("color", personRequest.eyesColor().name())
                && isEnumCorrect("color", personRequest.hairColor().name())
                && isEnumCorrect("nationality", personRequest.nationality().name())
        ){
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
        throw new IllegalParameterException("Incorrect input body");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons(
            @QueryParam("sort") List<String> sortParameters,
            @QueryParam("filter") List<String> filterParameters,
            @QueryParam("page") String page,
            @QueryParam("pageSize") String pageSize
    ) {
        Integer pageParam = null, pageSizeParam = null;

        if (page != null) {
            pageParam = Integer.parseInt(page);
            if (pageParam <= 0) {
                throw new IllegalParameterException("wrong page param");
            }
        }
        if (pageSize != null) {
            pageSizeParam = Integer.parseInt(pageSize);
            if (pageSizeParam <= 0) {
                throw new IllegalParameterException("wrong page size");
            }
        }


        List<String> sort = sortParameters == null
                ? List.of()
                : sortParameters.stream().filter(Predicate.not(String::isEmpty)).collect(Collectors.toList());
        List<String> filter = filterParameters == null
                ? new ArrayList<>()
                : filterParameters.stream().filter(Predicate.not(String::isEmpty)).collect(Collectors.toList());

        Page<Person> resultPage = personService.getPersonsFilter(sort, filter, pageParam, pageSizeParam);

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
        if (personId < 1 || personId > 2000000000){
            throw new IllegalParameterException("Person id must be > 1 and < 2000000000");
        }
        Person person = personService.getByPersonId(personId).get();
        return Response.ok(createPersonResponse(person)).build();
    }

    @GET
    @Path("/height/{operation}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonHeight(@PathParam("operation") String operation) {
        double height = personService.getHeight(operation);
        return switch (operation.toLowerCase()) {
            case "average" -> Response.ok(new AvgHeightResponse(height)).build();
            case "max" -> Response.ok(new MaxHeightResponse(height)).build();
            case "min" -> Response.ok(new MinHeightResponse(height)).build();
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
        if (personId < 1 || personId > 2000000000){
            throw new IllegalParameterException("Person id must be > 1 and < 2000000000");
        }
        personService.deleteByPersonId(personId);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{person_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePersonById(@PathParam("person_id") int personId, PersonRequest personRequest) {
        if (personId < 1 || personId > 2000000000){
            throw new IllegalParameterException("Person id must be > 1 and < 2000000000");
        }
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

    private boolean isEnumCorrect(String enumName, String enumValue) {
        return switch (enumName) {
            case "color" -> Color.fromValue(enumValue.toLowerCase()) != null;
            case "nationality" -> Nationality.fromValue(enumValue.toLowerCase()) != null;
            default -> false;
        };
    }

}
