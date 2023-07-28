package com.baseline.repository;

import com.baseline.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,
        Long>, BaseRepository<StudentEntity> {
}
