package com.geraldsaccount.killinary.model.dinner;

import java.util.List;
import java.util.UUID;

import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.mystery.Character;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "votes")
@Getter
@Setter
@With
public class Vote {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "vote_suspects", joinColumns = @JoinColumn(name = "vote_id"), inverseJoinColumns = @JoinColumn(name = "character_id"))
  private List<Character> suspects;

  @Column(columnDefinition = "TEXT")
  private String motive;
}
