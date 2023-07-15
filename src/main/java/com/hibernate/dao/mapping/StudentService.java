package com.hibernate.dao.mapping;

import com.hibernate.entityBase.service.BaseService;
import com.hibernate.model.StudentEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService extends BaseService<StudentEntity, StudentRepository> {

    @Override
    protected Class<StudentEntity> getEntityClass() {
        return StudentEntity.class;
    }

    public List<StudentEntity> getAll() {
        return repository.findAll();
    }

}
