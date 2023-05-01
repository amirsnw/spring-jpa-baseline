package com.hibernate.dto.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Order(2)
@DisplayName("Test Operator Methods on Operator Enum Class")
public class OperatorTest {

    @DisplayName("Test Converting JSON To Operator")
    @ParameterizedTest
    @MethodSource("JsonOperatorGenerator")
    void testDeserializationToOperator_WhenUsingJsonCreatorAnnotation_ShouldProduceAnOperator(String json, Operator expectedOperator) {

        // Arrange
        Operator operator = null;
        ObjectMapper mapper = new ObjectMapper();

        // Act
        try {
            operator = mapper
                    .readerFor(Operator.class)
                    .readValue(json);
        } catch (JsonProcessingException e) {
            fail("Unexpected Exception!");
        }

        assertEquals(expectedOperator, operator,
                () -> json + " Json does not belong to [" + expectedOperator + "] operator");
    }

    private static Stream<Arguments> JsonOperatorGenerator() {

        return Stream.of(
                Arguments.of("\"EQUAL\"", Operator.EQUAL),
                Arguments.of("\"NOT_EQUAL\"", Operator.NOT_EQUAL),
                Arguments.of("\"LIKE\"", Operator.LIKE),
                Arguments.of("\"BETWEEN\"", Operator.BETWEEN),
                Arguments.of("\"AFTER\"", Operator.AFTER),
                Arguments.of("\"BEFORE\"", Operator.BEFORE),
                Arguments.of("\"IN\"", Operator.IN)
        );
    }

    @DisplayName("Test Converting Operator To Json")
    @ParameterizedTest
    @MethodSource("operatorJsonGenerator")
    void testSerializationToJson_WhenUsingJsonValueAnnotation_ShouldProduceAnJson(Operator operator, String expectedJson) {

        // Arrange
        String jsonString = null;
        ObjectMapper mapper = new ObjectMapper();

        // Act
        try {
            jsonString = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(operator);
        } catch (JsonProcessingException e) {
            fail("Unexpected Exception!");
        }

        assertEquals(expectedJson, jsonString,
                () -> "[" + operator.name() + "] Operator does not equivalent to " + expectedJson + " json");
    }

    private static Stream<Arguments> operatorJsonGenerator() {

        return Stream.of(
                Arguments.of(Operator.EQUAL, "\"EQUAL\""),
                Arguments.of(Operator.NOT_EQUAL, "\"NOT_EQUAL\""),
                Arguments.of(Operator.LIKE, "\"LIKE\""),
                Arguments.of(Operator.BETWEEN, "\"BETWEEN\""),
                Arguments.of(Operator.AFTER, "\"AFTER\""),
                Arguments.of(Operator.BEFORE, "\"BEFORE\""),
                Arguments.of(Operator.IN, "\"IN\"")
        );
    }
}
