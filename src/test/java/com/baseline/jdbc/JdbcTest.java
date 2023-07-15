package com.baseline.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Order(1)
@DisplayName("Test Connection")
public class JdbcTest {

	@DisplayName("Connect to DB with Invalid Username or Password")
	@Test
	void TestConnection_WhenUsernameAndPasswordAreNotValid_ThenSQLExceptionHappens() {

		// Arrange
		String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres?useUnicode=true" +
				"&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false" +
				"&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true";
		String user = "NotValid";
		String pass = "NotValid";
		String expectedExceptionMessage = "FATAL: password authentication failed for user \"NotValid\"";

		// Act & Assert
		SQLException actualException = assertThrows(SQLException.class, () -> {
			// Act
			DriverManager.getConnection(jdbcUrl, user, pass);
		}, "Invalid parameters should throw exception on connection");

		// Assert
		assertEquals(expectedExceptionMessage, actualException.getMessage(),
				"Unexpected exception message");
	}

	@DisplayName("Connect to DB with Invalid Parameters")
	@Test
	void TestConnection_WhenJDBCUrlAreNotValid_ThenSQLExceptionHappens() {

		// Arrange
		String jdbcUrl = "NotValid";
		String user = "NotValid";
		String pass = "NotValid";
		String expectedExceptionMessage = "No suitable driver found for NotValid";

		// Act & Assert
		SQLException actualException = assertThrows(SQLException.class, () -> {
			// Act
			DriverManager.getConnection(jdbcUrl, user, pass);
		}, "Invalid parameters should throw exception on connection");

		// Assert
		assertEquals(expectedExceptionMessage, actualException.getMessage(),
				"Unexpected exception message");
	}

	@DisplayName("Connect to DB with Valid Parameters")
	@Test
	void TestConnection_WhenJDBCUrlUsernameAndPasswordAreValid_ThenNoExceptionHappens() {

		// Arrange
		String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres?useUnicode=true" +
				"&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false" +
				"&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true";
		String user = "postgres";
		String pass = "post123";

		// Act & Assert
		try {
			DriverManager.getConnection(jdbcUrl, user, pass);
		} catch (SQLException e) {
			fail("Unexpected Exception!");
		}
	}

	@DisplayName("Test DB driver")
	@Test
	void TestDriver_WhenDriverIsValid_ThenNoExceptionHappens() {

		// Arrange
		String driver = "org.postgresql.Driver";

		// Act & Assert
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			fail("Driver Exception!");
		}
	}
}



