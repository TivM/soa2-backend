package org.library.dto.request;


import org.library.dto.Coordinates;
import org.library.dto.Location;
import org.library.enums.Color;
import org.library.enums.Nationality;

public record PersonRequest (
        String name,
        Coordinates coordinates,
        double height,
        String birthday,
        String passportID,
        Color hairColor,
        Color eyesColor,
        Nationality nationality,
        Location location
        ) { }
