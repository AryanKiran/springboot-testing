package com.codingshuttle.TestingApp.repositories;
import com.codingshuttle.TestingApp.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

@DataJpaTest
class EmployeeRepositoryTest {
    private Employee employee;

    @BeforeEach
    void setUp(){
        employee =  Employee.builder()
                .id(1L)
                .name("Aryan")
                .email("aryanKiran@gmail.com")
                .salary(10000L)
                .build();
    }

    @Autowired
    private EmployeeRepository employeeRepository;
    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {
        //arrange,Given
        employeeRepository.save(employee);
        //Act,when
        List<Employee> employeeList = employeeRepository.findByEmail(employee.getEmail());

        //Assert,Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmployeeList() {
        String email = "notPresent.123@gmail.com";
//        When
        List<Employee> employeeList = employeeRepository.findByEmail(email);
//        Then
        assertThat(employeeList).isNotNull();

        //Arrange, Given
        String emailId = "aryan@gmail.com";
        //Act, When
        List<Employee> employeeList1 = employeeRepository.findByEmail(email);
        //Assert, Then
        assertThat(employeeList1).isNotNull();
    }
}
