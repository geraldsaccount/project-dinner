package com.geraldsaccount.killinary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.geraldsaccount.killinary.model.StoryConfigurationCharacter;
import com.geraldsaccount.killinary.model.id.StoryConfigurationCharacterId;

public interface StoryConfigurationCharacterRepository
        extends JpaRepository<StoryConfigurationCharacter, StoryConfigurationCharacterId> {

}
