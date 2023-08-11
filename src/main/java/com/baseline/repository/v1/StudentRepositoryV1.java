package com.baseline.repository.v1;

import com.baseline.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepositoryV1 extends JpaRepository<StudentEntity,
        Long>, BaseRepositoryV1<StudentEntity> {
}
