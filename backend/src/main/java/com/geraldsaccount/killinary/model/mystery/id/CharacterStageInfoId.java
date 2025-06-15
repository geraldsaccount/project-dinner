package com.geraldsaccount.killinary.model.mystery.id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
public class CharacterStageInfoId implements Serializable {
    private UUID stageId;
    private UUID characterId;

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        CharacterStageInfoId that = (CharacterStageInfoId) other;
        return Objects.equals(stageId, that.stageId) &&
                Objects.equals(characterId, that.characterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stageId, characterId);
    }
}
