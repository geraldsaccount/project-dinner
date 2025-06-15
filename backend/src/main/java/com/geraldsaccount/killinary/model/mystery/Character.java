package com.geraldsaccount.killinary.model.mystery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "characters")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String role;

    private Integer age;

    @Builder.Default
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    private Gender gender;

    @Column(name = "shop_description")
    private String shopDescription;

    @Column(name = "private_description")
    private String privateDescription;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "character_relationships", joinColumns = @JoinColumn(name = "character_id"))
    @MapKeyColumn(name = "related_character_id")
    private Map<UUID, String> relationships = new HashMap<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    @JoinTable(name = "character_stage_infos", joinColumns = @JoinColumn(name = "character_id"), inverseJoinColumns = @JoinColumn(name = "stage_info_id", unique = true))
    private List<CharacterStageInfo> stageInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Character character = (Character) o;
        return id != null && id.equals(character.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
