package com.baseline.entity;

import com.baseline.config.AppConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = AppConstants.TABLE_PREFIX + "student",
        // class level uniqueness across multiple columns
        uniqueConstraints = {@UniqueConstraint(columnNames = {"first_name", "last_name"})})
public class StudentEntity extends BaseAuditingEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // property level uniqueness for a single column
    @Column(name = "email", nullable = false, unique = true)
    private String email;

}




