package com.hibitbackendimproved.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @LastModifiedDate
    @Column(name = "updated_date_time", nullable = false)
    private LocalDateTime updateDateTime;

    // 생성자 추가
    protected BaseEntity() {
        this.createDateTime = LocalDateTime.now();
        this.updateDateTime = LocalDateTime.now();
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }
}
