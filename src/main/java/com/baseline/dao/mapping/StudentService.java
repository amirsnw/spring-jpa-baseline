package com.baseline.dao.mapping;

import com.baseline.entityBase.service.BaseService;
import com.baseline.model.StudentEntity;
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
