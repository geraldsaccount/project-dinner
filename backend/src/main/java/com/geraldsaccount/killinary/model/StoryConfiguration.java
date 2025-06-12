package com.geraldsaccount.killinary.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Entity
@Table(name = "story_configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class StoryConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @Column(name = "player_count", nullable = false)
    private Integer playerCount;

    @Column(name = "configuration_name", nullable = false)
    private String configurationName;

    @ManyToMany
    @JoinTable(name = "story_configuration_character", joinColumns = @JoinColumn(name = "story_configuration_id"), inverseJoinColumns = @JoinColumn(name = "character_id"))
    @Builder.Default
    private Set<Character> characters = new HashSet<>();
}