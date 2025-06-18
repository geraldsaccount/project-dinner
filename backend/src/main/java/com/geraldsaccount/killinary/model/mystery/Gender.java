package com.geraldsaccount.killinary.model.mystery;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private Gender(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null)
            return null;
        return switch (value.trim().toUpperCase()) {
            case "MALE" -> MALE;
            case "FEMALE" -> FEMALE;
            case "OTHER" -> OTHER;
            default -> throw new IllegalArgumentException("Unknown gender: " + value);
        };
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
