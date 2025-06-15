package com.geraldsaccount.killinary.model.mystery;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mysteries")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Mystery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    @Column(name = "is_published")
    private boolean isPublished = true;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "story_id")
    private Story story;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "mystery_characters", joinColumns = @JoinColumn(name = "mystery_id"), inverseJoinColumns = @JoinColumn(name = "character_id", unique = true))
    private List<Character> characters;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    @JoinTable(name = "mystery_stages", joinColumns = @JoinColumn(name = "mystery_id"), inverseJoinColumns = @JoinColumn(name = "stage_id"))
    private List<Stage> stages;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "mystery_setups", joinColumns = @JoinColumn(name = "mystery_id"), inverseJoinColumns = @JoinColumn(name = "setup_id", unique = true))
    private List<PlayerConfig> setups;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "crime_id")
    private Crime crime;
}
