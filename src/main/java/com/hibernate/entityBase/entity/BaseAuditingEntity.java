package com.hibernate.entityBase.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Audited
@Getter
@Setter
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseAuditingEntity extends BaseEntity {

    @CreatedBy
    @Column(
            name = "created_by",
            length = 50,
            updatable = false
    )
    @JsonIgnore
    private String createdBy;

    @LastModifiedBy
    @Column(
            name = "last_modified_by",
            length = 50
    )
    @JsonIgnore
    private String lastModifiedBy;

    protected BaseAuditingEntity(Long id) {
        super(id);
    }
}
