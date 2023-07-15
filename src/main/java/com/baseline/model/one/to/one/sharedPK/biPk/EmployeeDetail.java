package com.baseline.model.one.to.one.sharedPK.biPk;

import com.baseline.config.AppConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AppConstants.TABLE_PREFIX + "employee_detail")
public class EmployeeDetail {

    @Id
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Employee employee;
}
