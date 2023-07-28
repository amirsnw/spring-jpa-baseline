package com.baseline.service;

import com.baseline.entity.StudentEntity;
import com.baseline.exception.CustomException;
import com.baseline.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService extends BaseService<StudentEntity, StudentRepository> {

    @Override
    protected Class<StudentEntity> getEntityClass() {
        return StudentEntity.class;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public StudentEntity save(StudentEntity entity) {
        return repository.save(entity);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public StudentEntity saveWithREQUIRED(StudentEntity entity) {
        StudentEntity student = save(entity);
        try {
            callREQUIRED();
        } catch (CustomException e) {
            // handle exception
        }
        return student;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public StudentEntity callREQUIRED() {
        throw new CustomException("Rollback this transaction!");
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public StudentEntity saveWithREQUIRES_NEW(StudentEntity entity) {
        StudentEntity student = save(entity);
        try {
            callREQUIRES_NEW();
        } catch (CustomException e) {
            // handle exception
        }
        return student;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public StudentEntity callREQUIRES_NEW() {
        throw new CustomException("Rollback this transaction!");
    }
}
