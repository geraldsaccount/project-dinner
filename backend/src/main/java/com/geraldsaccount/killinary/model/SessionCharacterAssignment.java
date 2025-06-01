package com.geraldsaccount.killinary.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "session_character_assignments", uniqueConstraints = {
        // Defines the composite unique constraint
        @UniqueConstraint(columnNames = { "session_id", "character_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionCharacterAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "role_briefing_sent", nullable = false)
    @Builder.Default
    private Boolean roleBriefingSent = false;

    @PrePersist
    protected void onAssign() {
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
        if (roleBriefingSent == null) {
            roleBriefingSent = false;
        }
    }
}