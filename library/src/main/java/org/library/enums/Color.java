package org.library.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public enum Color {
    RED("red"),
    BLACK("black"),
    BLUE("blue"),
    ORANGE("orange");

    @Getter
    private final String value;

    public static Color fromValue(String value){
        return Arrays.stream(Color.values())
                .filter(e -> Objects.equals(e.getValue(), value))
                .findFirst()
                .orElse(null);
    }
}
