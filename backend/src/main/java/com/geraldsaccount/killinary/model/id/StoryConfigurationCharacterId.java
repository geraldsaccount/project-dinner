package com.geraldsaccount.killinary.model.id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryConfigurationCharacterId implements Serializable {
    private UUID storyConfigurationId;
    private UUID characterId;

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        StoryConfigurationCharacterId that = (StoryConfigurationCharacterId) other;
        return Objects.equals(storyConfigurationId, that.storyConfigurationId) &&
                Objects.equals(characterId, that.characterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storyConfigurationId, characterId);
    }
}