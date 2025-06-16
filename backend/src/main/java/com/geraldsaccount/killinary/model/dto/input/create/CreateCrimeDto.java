package com.geraldsaccount.killinary.model.dto.input.create;

import java.util.List;

public record CreateCrimeDto(List<String> criminalIds, String description) {

}
