package com.ilkinmehdiyev.myticket.domain.entity;

import com.ilkinmehdiyev.myticket.domain.enums.UserRole;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRole name;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users = new HashSet<>();
}
