package com.example.demo.client.webclient;

import com.example.demo.client.api.PersonsClient;
import org.library.dto.Coordinates;
import org.library.dto.Location;
import org.library.dto.request.PersonRequest;
import org.library.dto.response.EnumValuesResponse;
import org.library.dto.response.ListPersonResponse;
import org.library.dto.response.PersonResponse;
import org.library.enums.Color;
import org.library.enums.Nationality;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

public class PersonWebClient implements PersonsClient {
    private static final String BASE_URL = "https://localhost:8881/api/v1/persons";

    private final WebClient webClient;
    private final String baseUrl;

    public PersonWebClient(WebClient webClient) {
        this.webClient = webClient;
        this.baseUrl = BASE_URL;
    }

    public PersonWebClient(WebClient webClient, String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }
    @Override
    public PersonResponse addPerson(
            String name,
            Coordinates coordinates,
            double height,
            String birthday,
            String passportID,
            Color hairColor,
            Color eyesColor,
            Nationality nationality,
            Location location
    ) {
        System.out.println("LOOOOOOG!!!!!!!!!!!");
        return webClient
                .post()
                .uri(baseUrl)
                .bodyValue(
                        new PersonRequest(
                                name,
                                coordinates,
                                height,
                                birthday,
                                passportID,
                                hairColor,
                                eyesColor,
                                nationality,
                                location
                        )
                )
                .retrieve()
                .bodyToMono(PersonResponse.class)
                .block();
    }

    @Override
    public ListPersonResponse getAllPersons() {
        System.out.println("LOOOOOOG!!!!!!!!!!!" + LocalDateTime.now());
        return webClient
                .get()
                .uri(baseUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ListPersonResponse.class)
                .block();
    }

    @Override
    public PersonResponse getByPersonId(Integer personId) {
        System.out.println("LOOOOOOG!!!!!!!!!!!");
        return webClient
                .get()
                .uri(baseUrl + "/" + personId.toString())
                .retrieve()
                .bodyToMono(PersonResponse.class)
                .block();
    }

    @Override
    public Void deleteByPersonId(Integer personId) {
        return webClient
                .delete()
                .uri(baseUrl + "/" + personId.toString())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public PersonResponse updateByPersonId(
            Integer personId,
            String name,
            Coordinates coordinates,
            Double height,
            String birthday,
            String passportID,
            Color hairColor,
            Color eyesColor,
            Nationality nationality,
            Location location
    ) {
        return webClient
                .put()
                .uri(baseUrl + "/" + personId.toString())
                .bodyValue(
                        new PersonRequest(name,
                                coordinates,
                                height,
                                birthday,
                                passportID,
                                hairColor,
                                eyesColor,
                                nationality,
                                location
                        )
                )
                .retrieve()
                .bodyToMono(PersonResponse.class)
                .block();
    }

    @Override
    public Double getHeight(String operation) {
        System.out.println("LOOOOOOG!!!!!!!!!!!" + LocalDateTime.now());
        return webClient
                .get()
                .uri(baseUrl + "/height/" + operation)
                .retrieve()
                .bodyToMono(Double.class)
                .block();
    }

    @Override
    public EnumValuesResponse getPersonEnum(String enumName) {
        System.out.println("LOOOOOOG!!!!!!!!!!!" + LocalDateTime.now());
        return webClient
                .get()
                .uri(baseUrl + "/enum/" + enumName)
                .retrieve()
                .bodyToMono(EnumValuesResponse.class)
                .block();
    }
}
