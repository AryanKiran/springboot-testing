package com.codingshuttle.TestingApp.controllers;


import com.codingshuttle.TestingApp.dto.EmployeeDto;
import com.codingshuttle.TestingApp.entities.Employee;
import com.codingshuttle.TestingApp.repositories.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeControllerTestIt extends AbstractIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee; //never autowire an entity because they are managed by hibernate so no beans created for them.


    private EmployeeDto testEmployeeDto;

    @BeforeEach
    void setUp() {
        testEmployee = Employee.builder().name("Aryan").email("aryanKiran@gmail.com").salary(10000L).build();
        testEmployeeDto = EmployeeDto.builder().name("Aryan").email("aryanKiran@gmail.com").salary(10000L).build();
        employeeRepository.deleteAll();
    }

    @Test
    void testGetEmployeeById_Success() {
        Employee savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.get().uri("/employees/{id}", savedEmployee.getId()).exchange().expectStatus().isOk().expectBody().jsonPath("$.id").isEqualTo(savedEmployee.getId());
    }

    @Test
    void testGetEmployeeById_Failure() {
        webTestClient.get().uri("/employees/200").exchange().expectStatus().isNotFound();
    }

    @Test
    void testCreateEmployee_WhenEmployeeAlreadyExists_ThenReturnError() {
        Employee savedEmployee = employeeRepository.save(testEmployee);

        // act + assert
        webTestClient.post()
                .uri("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testEmployeeDto)
                .exchange()

                // HTTP status
                .expectStatus().is5xxServerError();
    }

    @Test
    void testCreateEmployee_WhenValidRequest_ThenReturnCreatedEmployee() {
        webTestClient.post().uri("/employees").contentType(MediaType.APPLICATION_JSON).bodyValue(testEmployeeDto).exchange()
                // HTTP assertions
                .expectStatus().isCreated().expectHeader().contentType(MediaType.APPLICATION_JSON)
                // Body assertions
                .expectBody(EmployeeDto.class).value(responseDto -> {
                    assertThat(responseDto.getId()).isNotNull();
                    assertThat(responseDto.getName()).isEqualTo("Aryan");
                    assertThat(responseDto.getEmail()).isEqualTo("aryanKiran@gmail.com");
                    assertThat(responseDto.getSalary()).isEqualTo(10000L);
                });
    }

    @Test
    void testUpdateEmployee_WhenValidId_ThenReturnUpdatedEmployee() {

        // arrange
        Employee savedEmployee = employeeRepository.save(testEmployee);

        EmployeeDto updateRequest = EmployeeDto.builder()
                .name("Aryan Updated")
                .email("aryanKiran@gmail.com") // email must stay same
                .salary(20000L)
                .build();

        // act + assert
        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .value(response -> {
                    assertThat(response.getId()).isEqualTo(savedEmployee.getId());
                    assertThat(response.getName()).isEqualTo("Aryan Updated");
                    assertThat(response.getEmail()).isEqualTo("aryanKiran@gmail.com");
                    assertThat(response.getSalary()).isEqualTo(20000L);
                });
    }

    @Test
    void testUpdateEmployee_WhenInValidId_ThenReturnException(){
        webTestClient.put()
                .uri("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateEmployee_WhenUpdatingEmployeeEmail_ThenThrowException(){
        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setEmail("aryanK@");
        testEmployeeDto.setName("aryan");

        webTestClient.put()
                .uri("/employees/{id}",savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testDeleteEmployee_WhenValidId(){
        Employee savedEmployee = employeeRepository.save(testEmployee);
        webTestClient.delete()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteEmployee_WhenInValidId(){
        webTestClient.delete()
                .uri("/employees/2")
                .exchange()
                .expectStatus().isNotFound();
    }

}
