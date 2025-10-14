package com.huy.linguahub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.huy.linguahub.secutiry.SecurityUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@JsonIgnoreProperties(value = { "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" }, allowGetters = true)
public abstract class AbstractAuditingEntity<T> {
    public abstract T getId();

    private boolean deleted = false;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "created_date", updatable = false)
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @PrePersist
    public void prePersist() {
        this.createdBy = SecurityUtils.getCurrentUserLogin().orElse("system");

        this.createdDate = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedBy = SecurityUtils.getCurrentUserLogin().orElse("system");
        this.lastModifiedDate = Instant.now();

        if (this.deleted && this.deletedDate == null) {
            this.deletedBy = SecurityUtils.getCurrentUserLogin().orElse("system");
            this.deletedDate = Instant.now();
        }
    }
}
