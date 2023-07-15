package com.hibernate.entityBase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity implements Serializable, Comparable<BaseEntity> {

    @Id
    @GeneratedValue
    @Column(
            name = "id",
            nullable = false,
            unique = true
    )
    private Long id;

    @CreatedDate
    @Column(
            name = "created_date",
            updatable = false
    )
    private Instant createdDate = Instant.now();

    @LastModifiedDate
    @Column(
            name = "last_modified_date"
    )
    private Instant lastModifiedDate = Instant.now();

    @Column(
            name = "deleted",
            nullable = false
    )
    @ColumnDefault("false")
    private boolean deleted = false;

    protected BaseEntity(Long id) {
        this.id = id;
    }

    public int compareTo(BaseEntity o) {
        if (o == null) {
            return -1;
        } else if (this == o) {
            return 0;
        } else {
            return this.getId().equals(o.getId()) ? 0 : -1;
        }
    }
}
