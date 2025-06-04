package com.geraldsaccount.killinary.model;

import com.geraldsaccount.killinary.model.id.StoryConfigurationCharacterId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "story_configuration_character")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryConfigurationCharacter {
    @EmbeddedId
    private StoryConfigurationCharacterId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("storyConfigurationId")
    @JoinColumn(name = "story_configuration_id", nullable = false)
    private StoryConfiguration storyConfiguration;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("characterId")
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    public StoryConfigurationCharacter(StoryConfiguration storyConfiguration, Character character) {
        this.storyConfiguration = storyConfiguration;
        this.character = character;
        this.id = new StoryConfigurationCharacterId(storyConfiguration.getId(), character.getId());
    }
}