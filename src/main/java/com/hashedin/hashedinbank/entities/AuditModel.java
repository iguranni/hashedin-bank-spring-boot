package com.hashedin.hashedinbank.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@SuperBuilder
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel {

    @CreationTimestamp
    @Column(name = "CREATED_DT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, length = 50, updatable = false)
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "UPDATED_DT", nullable = false, updatable = false)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "UPDATED_BY", nullable = false, length = 50, updatable = false)
    private String updatedBy;


    @PrePersist
    public void prePersist() {
        this.createdBy = getUser();
        this.updatedBy = getUser();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedBy = getUser();
    }

    private String getUser() {
        return "Iram E Saba";
    }
}