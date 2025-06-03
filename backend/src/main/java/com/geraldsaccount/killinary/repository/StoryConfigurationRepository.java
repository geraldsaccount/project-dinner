package com.geraldsaccount.killinary.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.geraldsaccount.killinary.model.StoryConfiguration;

public interface StoryConfigurationRepository extends JpaRepository<StoryConfiguration, UUID> {

}
