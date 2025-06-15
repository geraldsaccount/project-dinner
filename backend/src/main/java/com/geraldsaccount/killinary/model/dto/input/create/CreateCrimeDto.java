package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.List;
import java.util.UUID;

public record CreateCrimeDto(List<UUID> criminalIds, String description) {

}
