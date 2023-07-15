package com.hibernate.dao.mapping;

import com.hibernate.entityBase.repository.BaseRepository;
import com.hibernate.model.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,
        Long>, BaseRepository<StudentEntity> {
}
