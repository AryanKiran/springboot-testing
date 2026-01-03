package com.codingshuttle.TestingApp.services;

import com.codingshuttle.TestingApp.dto.EmployeeDto;
import com.codingshuttle.TestingApp.entities.Employee;
import com.codingshuttle.TestingApp.exceptions.ResourceNotFoundException;
import com.codingshuttle.TestingApp.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    private Employee mockEmployee;

    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setUp() {
        Long id = 1L;
         mockEmployee = Employee.builder()
                .id(id)
                .name("aryan")
                .email("aryankiran28@gmail.com")
                .salary(200L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee,EmployeeDto.class);
    }

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto() {
        //assign
        Long id = mockEmployee.getId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee)); //stubbing define the behaviour of the mock object.
        //act
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        //assert
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository).findById(1L);//checks whether employeeRepository method was called or not.
        //verify(employeeRepository).findById(2L);
        //verify(employeeRepository).findById(null);
        //verify(employeeRepository,atLeast(2)).findById(1L);
        //verify(employeeRepository,atMost(2)).findById(2L);
    }

    @Test
    void testGetEmployeeById_WhenEmployeeIsNotPresent_ThenThrowException(){
        //Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        //When &
        //Then
        assertThatThrownBy(()->employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee() {
        //assign
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);
        //act
        EmployeeDto employeeDto = employeeService.createNewEmployee(mockEmployeeDto);
        //assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        Employee capturedEmployee = employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }

    @Test
    void testCreateNewEmployee_WhenEmailIsAlreadyPresent_ThenThrowException(){
        //assign
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));
        //act + assert
        assertThatThrownBy(()->employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: " + mockEmployeeDto.getEmail());

        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenValidId_ThenUpdateEmployee(){
        //arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.save(mockEmployee)).thenReturn(mockEmployee);
        //act
        EmployeeDto employee = employeeService.updateEmployee(1L,mockEmployeeDto);
        //assert
        assertThat(employee).isNotNull();
        assertThat(employee.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(mockEmployee);
    }

    @Test
    void testUpdateEmployee_WhenInValidEmailId_ThenThrowException(){
        //arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setEmail("ayKiran@gmail.com");

        assertThatThrownBy(()->employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository, never()).save(any());

    }
    @Test
    void testUpdateEmployee_WhenInValidId_ThenThrowException(){
        //arrange
        Long invalidId = 2L;
        when(employeeRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->employeeService.updateEmployee(invalidId,mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: " + invalidId);

        verify(employeeRepository).findById(invalidId);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testDeleteEmployee_WhenValidId(){
        //arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);
        //When
            employeeService.deleteEmployee(1L);
        //Then
        verify(employeeRepository).existsById(anyLong());
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_WhenInValidId_ThenThrowException(){
        //arrange
        when(employeeRepository.existsById(1L)).thenReturn(false);

        //assert + then

        assertThatThrownBy(()-> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: " + 1L);

        verify(employeeRepository,never()).deleteById(1L);
    }
}
