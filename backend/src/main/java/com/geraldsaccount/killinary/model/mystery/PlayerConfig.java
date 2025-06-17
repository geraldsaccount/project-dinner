package com.geraldsaccount.killinary.model.mystery;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "configs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PlayerConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "player_count")
    private Integer playerCount;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "config_characters", joinColumns = @JoinColumn(name = "config_id"), inverseJoinColumns = @JoinColumn(name = "character_id"))
    private Set<Character> characters;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PlayerConfig that = (PlayerConfig) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
