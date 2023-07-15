package com.baseline.dao.mapping;

import com.baseline.entityBase.repository.BaseRepository;
import com.baseline.model.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,
        Long>, BaseRepository<StudentEntity> {
}
