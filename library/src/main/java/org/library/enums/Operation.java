package org.library.enums;

import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Operation {
    MIN("min"),
    MAX("max"),
    AVERAGE("average");

    @Getter
    private final String value;

    public static Operation fromValue(String value){
        return Arrays.stream(Operation.values())
                .filter(e -> Objects.equals(e.getValue(), value))
                .findFirst()
                .orElse(null);
    }
}


