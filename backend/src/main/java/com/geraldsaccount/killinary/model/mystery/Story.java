package com.geraldsaccount.killinary.model.mystery;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "shop_description", columnDefinition = "TEXT")
    private String shopDescription;

    @Column(name = "banner_image", columnDefinition = "bytea")
    private byte[] bannerImage;

    @Column(name = "rules", columnDefinition = "TEXT")
    private String rules;

    @Column(name = "setting", columnDefinition = "TEXT")
    private String setting;

    @Column(name = "briefing", columnDefinition = "TEXT")
    private String briefing;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Story that = (Story) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
