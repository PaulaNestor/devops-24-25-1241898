package com.greglturnquist.payroll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class EmployeeTest {

    @Test
    void shouldCreateEmployee() {

        //act
        Employee employee1 = new Employee("John", "Doe", "Developer", 5, "johndoe@gmail.com");
        Employee employee2 = new Employee("John", "Doe", "Developer", 0, "johndoe@gmail.com");

        //assert
        assertNotNull(employee1);
        assertNotNull(employee2);

    }

    public static Stream<Arguments> provideInvalidParameters() {
        return Stream.of(
                arguments(null, "Doe", "Developer", 5, "johndoe@gmail.com", "First name cannot be null or empty!"),
                arguments("", "Doe", "Developer", 5, "johndoe@gmail.com", "First name cannot be null or empty!"),
                arguments(" ", "Doe", "Developer", 5, "johndoe@gmail.com", "First name cannot be null or empty!"),
                arguments("John", null, "Developer", 5, "johndoe@gmail.com", "Last name cannot be null or empty!"),
                arguments("John", "", "Developer", 5, "johndoe@gmail.com", "Last name cannot be null or empty!"),
                arguments("John", " ", "Developer", 5, "johndoe@gmail.com", "Last name cannot be null or empty!"),
                arguments("John", "Doe", null, 5, "johndoe@gmail.com", "Description cannot be null or empty!"),
                arguments("John", "Doe", "", 5, "johndoe@gmail.com", "Description cannot be null or empty!"),
                arguments("John", "Doe", " ", 5, "johndoe@gmail.com", "Description cannot be null or empty!"),
                arguments("John", "Doe", "Developer", null, "johndoe@gmail.com", "Job Years must be a positive number!"),
                arguments("John", "Doe", "Developer", -1, "johndoe@gmail.com", "Job Years must be a positive number!"),
                arguments("John", "Doe", "Developer", 5, null, "Email cannot be null or empty!"),
                arguments("John", "Doe", "Developer", 5, "", "Email cannot be null or empty!"),
                arguments("John", "Doe", "Developer", 5, " ", "Email cannot be null or empty!"),
                arguments("John", "Doe", "Developer", 5, "johndoegmail.com", "Email must contain '@'!")
        );
    }
    @ParameterizedTest
    @MethodSource("provideInvalidParameters")
    void shouldReturnAnExceptionIfTheParametersAreInvalid(String firstName, String lastName, String description, Integer jobYears, String email, String expectedMessage) {
        //arrange

        //act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));

        //assert
        assertEquals(exception.getMessage(), expectedMessage);
    }


  
}