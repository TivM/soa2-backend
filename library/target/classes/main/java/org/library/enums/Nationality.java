package org.library.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public enum Nationality {
    RUSSIAN("russian"),
    ENGLISH("english"),
    BRAZILIAN("brazilian");

    @Getter
    private final String value;

    public static Nationality fromValue(String value){
        return Arrays.stream(Nationality.values())
                .filter(e -> Objects.equals(e.getValue(), value))
                .findFirst()
                .orElse(null);
    }
}
