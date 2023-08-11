package com.baseline.repository;

import com.baseline.entity.StudentEntity;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,
        Long>, BaseRepository<StudentEntity> {

    // Query-level timeout :
    // The query will wait for a maximum of 3 seconds (3000 milliseconds)
    // for the result before timing out.
    @QueryHints(@QueryHint(name = "javax.persistence.query.timeout", value = "3000"))
    @Query(value = "SELECT s FROM hb_student s WHERE s.email = :email")
    StudentEntity findByEmailWithTimeout(String email);

    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("SELECT s FROM hb_student s")
    List<StudentEntity> findAllCacheable();

    @QueryHints(value = {
            @QueryHint(name = "javax.persistence.lock.timeout", value = "2000"),
            @QueryHint(name = "javax.persistence.lock.mode", value = "PESSIMISTIC_WRITE")
    })
    @Query("SELECT s FROM hb_student s WHERE s.id = :id")
    StudentEntity findStudentForUpdate(@Param("id") Long id);

    @QueryHints(value = @QueryHint(name = "javax.persistence.cache.storeMode", value = "REFRESH"))
    @Query("SELECT s FROM hb_student s WHERE s.id = :id")
    StudentEntity findStudentWithCacheRefresh(@Param("id") Long id);
}
