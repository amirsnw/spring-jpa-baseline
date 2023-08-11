package com.baseline.criteria;

import com.baseline.common.dto.SearchDto;
import com.baseline.common.model.SearchResultModel;
import com.baseline.entity.StudentEntity;
import com.baseline.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Criteria Main Version")
@SpringBootTest
public class CriteriaMainTest {

    @Autowired
    StudentRepository repository;

    @BeforeEach
    void setupDB() {
        repository.deleteAll();
        repository.save(StudentEntity.builder().firstName("Yair").lastName("Violeta").email("yair@mail.com").build());
        repository.save(StudentEntity.builder().firstName("Case").lastName("Tertia").email("case@mail.com").build());
    }

    @Test
    void searchWithoutArgumentTest() {
        assertThrows(NullPointerException.class, () -> repository.search(StudentEntity.class, null),
                "Searching students by null searchDto should not throw any exception");
    }

    @Test
    void searchWithFiltersTest() {
        SearchResultModel<StudentEntity> searchResult;
        SearchDto searchDto = new SearchDto();
        searchDto.and().field("firstName").eq("Yair");
        assertDoesNotThrow(() -> repository.search(StudentEntity.class, searchDto),
                "Searching students by filters should not throw any exception");

        searchResult = repository.search(StudentEntity.class, searchDto);
        assertEquals(1, searchResult.getResult().size());
    }
}



