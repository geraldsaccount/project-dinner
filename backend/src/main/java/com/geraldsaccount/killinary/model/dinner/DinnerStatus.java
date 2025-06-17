package com.geraldsaccount.killinary.model.dinner;

import lombok.Getter;

@Getter
public enum DinnerStatus {
    CREATED("Created"),
    IN_PROGRESS("In Progress"),
    PAUSED("Paused"),
    CONCLUDED("Concluded"),
    CANCELED("Canceled");

    private final String value;

    private DinnerStatus(String value) {
        this.value = value;
    }

}
