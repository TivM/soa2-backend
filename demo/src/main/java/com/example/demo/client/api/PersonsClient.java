package com.example.demo.client.api;

import org.library.dto.Coordinates;
import org.library.dto.Location;
import org.library.dto.response.EnumValuesResponse;
import org.library.dto.response.PersonResponse;
import org.library.enums.Color;
import org.library.enums.Nationality;

import java.util.List;
import java.util.Optional;

public interface PersonsClient {
    PersonResponse addPerson(String name,
                             Coordinates coordinates,
                             double height,
                             String birthday,
                             String passportID,
                             Color hairColor,
                             Color eyesColor,
                             Nationality nationality,
                             Location location
    );
    PersonResponse[] getAllPersons();
    PersonResponse getByPersonId(Integer personId);
    Void deleteByPersonId(Integer personId);
    PersonResponse updateByPersonId(
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
    );

    Double getHeight(String operation);

    EnumValuesResponse getPersonEnum(String enumName);
}
