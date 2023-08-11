package com.baseline.repository;

import com.baseline.entity.StudentEntity;
import com.baseline.entity.one.to.many.uni.CustomerEntity;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,
        Long>, BaseRepository<CustomerEntity> {

    @EntityGraph(value = "Customer.orders", type = EntityGraph.EntityGraphType.FETCH)
    Optional<CustomerEntity> findById(Long id);


    /* any attempt to modify the database within that
     * transaction will result in an exception being
     * thrown.
     *
     * does not prevent modifications to the entity
     * object itself if it is retrieved and modified
     * outside the repository method.
     */
    @Transactional(readOnly = true)
    Optional<CustomerEntity> findByName(String name);
}
