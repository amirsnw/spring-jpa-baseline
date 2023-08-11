package com.baseline.criteria;

import com.baseline.common.model.SearchResultModel;
import com.baseline.entity.StudentEntity;
import com.baseline.repository.v1.StudentRepositoryV1;
import com.baseline.repository.v1.dto.Filter;
import com.baseline.repository.v1.dto.FilterWrapper;
import com.baseline.repository.v1.dto.Operator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test Criteria First Version")
@SpringBootTest
public class CriteriaV1Test {

    @Autowired
    StudentRepositoryV1 repository;

    @BeforeEach
    void setupDB() {
        repository.deleteAll();
        repository.save(StudentEntity.builder().firstName("Yair").lastName("Violeta").email("yair@mail.com").build());
        repository.save(StudentEntity.builder().firstName("Case").lastName("Tertia").email("case@mail.com").build());
    }

    @Test
    void searchWithoutArgumentTest() {
        assertDoesNotThrow(() -> repository.search(StudentEntity.class, null, null, null),
                "Searching students by null inputs should not throw any exception");
    }

    @Test
    void searchWithFiltersTest() {
        SearchResultModel<StudentEntity> searchResult;
        FilterWrapper filterWrapper = FilterWrapper.getBuilder()
                .addFilter(Filter.builder().property("firstName").operator(Operator.EQUAL).value("Yair").build())
                .build();
        assertDoesNotThrow(() -> repository.search(StudentEntity.class, filterWrapper, null, null),
                "Searching students by filters should not throw any exception");

        searchResult = repository.search(StudentEntity.class, filterWrapper, null, null);
        assertEquals(1, searchResult.getResult().size());
    }
}



