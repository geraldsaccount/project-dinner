package com.geraldsaccount.killinary.repository;

import java.util.Optional;

public interface CodedRepository<T> {
    Optional<T> findByCode(String code);

    boolean existsByCode(String code);
}
