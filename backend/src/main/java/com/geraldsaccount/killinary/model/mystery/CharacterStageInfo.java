package com.geraldsaccount.killinary.model.mystery;

import java.util.List;

import com.geraldsaccount.killinary.model.mystery.id.CharacterStageInfoId;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "character_stage_infos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CharacterStageInfo {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "stageId", column = @Column(name = "stage_id")),
            @AttributeOverride(name = "characterId", column = @Column(name = "stage_character_id"))
    })
    private CharacterStageInfoId id;

    @Column(name = "character_stage_order", nullable = false)
    private Integer order;

    @Column(name = "objective_prompt")
    private String objectivePrompt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("event_order ASC")
    @JoinTable(name = "stage_info_events", joinColumns = {
            @JoinColumn(name = "stage_id", referencedColumnName = "stage_id"),
            @JoinColumn(name = "stage_character_id", referencedColumnName = "stage_character_id")
    }, inverseJoinColumns = @JoinColumn(name = "event_id", unique = true))
    private List<StageEvent> events;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CharacterStageInfo that = (CharacterStageInfo) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}