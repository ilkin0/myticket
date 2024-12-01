package com.ilkinmehdiyev.myticket.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Getter
@Setter
public class Audit {

  @Column(name = "is_active")
  private Boolean isActive;

  @CreatedDate
  @Column(name = "created_at")
  private Instant createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private Instant updatedAt;

  @CreatedBy
  @Column(name = "created_by")
  private Long createdBy;

  @LastModifiedBy
  @Column(name = "updated_by")
  private Long updatedBy;

  @PrePersist
  protected void onCreate() {
    createdAt = updatedAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Instant.now();
  }
}
