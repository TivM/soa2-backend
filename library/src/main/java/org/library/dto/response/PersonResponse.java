package org.library.dto.response;

import org.library.dto.Coordinates;
import org.library.dto.Location;
import org.library.enums.Color;
import org.library.enums.Nationality;

public record PersonResponse(
        int id,
        String name,
        Coordinates coordinates,
        String creationDate,
        double height,
        String birthday,
        String passportID,
        Color hairColor,
        Color eyesColor,
        Nationality nationality,
        Location location
) { }
