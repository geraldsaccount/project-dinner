package com.geraldsaccount.killinary.model;

import lombok.Getter;

@Getter
public enum SessionStatus {
  CREATED("Created"),
  WAITING("Waiting"),
  IN_PROGRESS("In Progress"),
  PAUSED("Paused"),
  CONCLUDED("Concluded"),
  CANCELED("Canceled");

  private final String value;

  private SessionStatus(String value) {
    this.value = value;
  }

}
