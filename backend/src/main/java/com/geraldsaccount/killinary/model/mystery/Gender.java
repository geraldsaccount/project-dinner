package com.geraldsaccount.killinary.model.mystery;

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
}
