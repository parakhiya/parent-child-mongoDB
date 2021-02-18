package com.example.parentChildMongo.service;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.parentChildMongo.entity.Employee;
import com.example.parentChildMongo.entity.EmployeeRequestBody;
import com.example.parentChildMongo.entity.EmployeeResponseBody;
import com.example.parentChildMongo.repository.EmployeeRepository;
import com.example.parentChildMongo.service.impl.EmployeeServiceImpl;

public class EmployeeServiceImplTest {
  private final static String ID = "6020e21aaf6f7e7aa0b4a2d9";
  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock
  private EmployeeRepository employeeRepository;
  @InjectMocks
  private EmployeeService employeeService = new EmployeeServiceImpl();
  private Employee employee;
  private Employee employeeException;
  private EmployeeRequestBody employeeRequestBody;


  private EmployeeResponseBody createResponseBody(Employee employee) {
    EmployeeResponseBody employeeResponseBody = new EmployeeResponseBody();
    employeeResponseBody.setFirstName(employee.getFirstName());
    employeeResponseBody.setLastName(employee.getLastName());
    employeeResponseBody.setSalary(employee.getSalary());
    if (!(employee.getManager() == null))
      employeeResponseBody.setManagerName(employee.getManager().getFirstName());
    return employeeResponseBody;
  }

  @Test
  public void testFindEmployee_whenEmployeeIsPresentTest() {
    when(employeeRepository.findById(ID)).thenReturn(java.util.Optional.of(employee));
    EmployeeResponseBody employee1 = employeeService.find(ID);
    assertEquals(createResponseBody(employee), employee1);
  }

  @Test
  public void testFindEmployee_whenEmployeeIsNotPresentTest() {
    Optional<Employee> employee = Optional.empty();
    when(employeeRepository.findById(ID)).thenReturn(employee);
    //do like these everywhere
    boolean isSuccess = true;

    try {
      employeeService.find(ID);
    } catch (Exception e) {
      isSuccess = false;
      assertEquals("Employee with id " + ID + " does not exist", e.getMessage());
    }
    assertFalse(isSuccess);
  }

  @Test
  public void testFindEmployee_whenEmployeeIsNull() {

    try {
      employeeService.find(null);
    } catch (Exception e) {
      assertEquals("Employee with id null does not exist", e.getMessage());
    }
  }

  @Test
  public void testGetAllEmployees() {
    List<Employee> list = new ArrayList<>();
    list.add(employee);
    Pageable pageable = PageRequest.of(2, 10);
    Page<Employee> pageList = new PageImpl<>(list, pageable, 10);

    when(employeeRepository.findAll(pageable)).thenReturn(pageList);
    List<EmployeeResponseBody> employee1 = employeeService.getAllEmployees(2);
    assertEquals(createResponseBody(list.get(0)), employee1.get(0));
  }


  @Test
  public void testCreateEmployee_whenEmployeeIsPresentTest() {
    when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
    when(employeeRepository.save(any())).thenReturn(employee);
    employeeService.create(employeeRequestBody);
    verify(employeeRepository, times(2)).findById(anyString());
    verify(employeeRepository, times(2)).save(any());
  }

  @Test
  public void testCreateEmployee_whenManagerIdIsNull() {
    EmployeeRequestBody employeeRequestBody = new EmployeeRequestBody("abhi", "para", null, null);
    when(employeeRepository.save(any())).thenReturn(employee);
    employeeService.create(employeeRequestBody);
    verify(employeeRepository, times(1)).save(any());

  }

  @Test
  public void testCreateEmployee_whenEmployeeIsNotPresentTest() {
    Optional<Employee> employee = Optional.empty();
    assertFalse(employee.isPresent());
    when(employeeRepository.findById(ID)).thenReturn(employee);
    EmployeeRequestBody employeeRequestBody = new EmployeeRequestBody("abhi", "para", ID, null);
    try {
      employeeService.create(employeeRequestBody);
    } catch (Exception e) {
      assertEquals("Manager with id 6020e21aaf6f7e7aa0b4a2d9 does not exist", e.getMessage());
    }
  }

  @Test
  public void testFindHierarchy_whenEmployeeIsPresentTest() {
    Employee employeeThisThird = new Employee();
    employeeThisThird.setId("6020e21aaf6f7e7aa0b4a2dg");
    employeeThisThird.setFirstName("abhi");
    employeeThisThird.setLastName("para");
    employeeThisThird.setManager(null);

    Employee employeeThisSecond = new Employee();
    employeeThisSecond.setId("6020e21aaf6f7e7aa0b4a2df");
    employeeThisSecond.setFirstName("abhi");
    employeeThisSecond.setLastName("para");
    employeeThisSecond.setManager(employeeThisThird);

    Employee employeeThis = new Employee();
    employeeThis.setId("6020e21aaf6f7e7aa0b4a2d9");
    employeeThis.setFirstName("abhi");
    employeeThis.setLastName("para");
    employeeThis.setManager(employeeThisSecond);


    when(employeeRepository.findById("6020e21aaf6f7e7aa0b4a2d9")).thenReturn(Optional.of(employeeThis));
    when(employeeRepository.findById("6020e21aaf6f7e7aa0b4a2df")).thenReturn(Optional.of(employeeThisSecond));
    when(employeeRepository.findById("6020e21aaf6f7e7aa0b4a2dg")).thenReturn(Optional.of(employeeThisThird));

    List<EmployeeResponseBody> employees = employeeService.findHierarchy("6020e21aaf6f7e7aa0b4a2d9");
    assertEquals(createResponseBody(employeeThisSecond), employees.get(0));
    assertEquals(createResponseBody(employeeThisThird), employees.get(1));
  }

  @Test
  public void testFindHierarchy_whenEmployeeIsNotPresentTest() {
    Optional<Employee> employee = Optional.empty();
    assertFalse(employee.isPresent());
    when(employeeRepository.findById(ID)).thenReturn(employee);
    try {
      employeeService.findHierarchy(ID);
    } catch (Exception e) {
      assertEquals("Employee with id 6020e21aaf6f7e7aa0b4a2d9 does not exist", e.getMessage());
    }
  }

  @Test
  public void testFindHierarchy_whenEmployeeIsNull() {

    try {
      employeeService.findHierarchy(null);
    } catch (Exception e) {
      assertEquals("Employee with id null does not exist", e.getMessage());
    }

  }

  @Test
  public void testDeleteEmployee_whenEmployeeIsPresentTest() {
    when(employeeRepository.findById("6020e21aaf6f7e7aa0b4a2d9")).thenReturn(java.util.Optional.of(employee));
    when(employeeRepository.findById("6020e21aaf6f7e7aa0b4a2df")).thenReturn(java.util.Optional.of(employee));
    employeeService.delete("6020e21aaf6f7e7aa0b4a2d9", "6020e21aaf6f7e7aa0b4a2df");
    verify(employeeRepository, times(2)).findById(anyString());
  }

  @Test
  public void testDeleteEmployee_whenEmployeeIsNotPresentTest() {
    Optional<Employee> employee = Optional.empty();
    assertFalse(employee.isPresent());
    when(employeeRepository.findById(ID)).thenReturn(employee);
    try {
      employeeService.delete(ID, "6020e21aaf6f7e7aa0b4a2df");
    } catch (Exception e) {
      assertEquals("Employee with id 6020e21aaf6f7e7aa0b4a2d9 does not exist", e.getMessage());
    }

  }

  @Test
  public void testDeleteEmployee_whenEmployeeIsNull() {

    try {
      employeeService.delete(null, "6020e21aaf6f7e7aa0b4a2df");
    } catch (Exception e) {
      assertEquals("Employee with id 6020e21aaf6f7e7aa0b4a2df does not exist", e.getMessage());
    }
  }

  @Test
  public void testDeleteEmployee_whenParentManagerIsNotPresentTest() {
    Optional<Employee> employee = Optional.empty();
    when(employeeRepository.findById(ID)).thenReturn(Optional.ofNullable(employeeException));
    when(employeeRepository.findById("6020e21aaf6f7e7aa0b4a2df")).thenReturn(employee);
    try {
      employeeService.delete(ID, "6020e21aaf6f7e7aa0b4a2df");
    } catch (Exception e) {
      assertEquals("Employee with id 6020e21aaf6f7e7aa0b4a2df does not exist", e.getMessage());
    }
  }

  @Test
  public void testGetBySalary() {

    when(employeeRepository.getBySalary(1000)).thenReturn(Arrays.asList(employee));
    List<Employee> employees = employeeService.getBySalary(1000);
    assertEquals(employee, employees.get(0));

  }

  @Test
  public void testUpdate_WhenEmployeeIsPresent() {
    when(employeeRepository.findById("6020e21aaf6f7e7aa0b4a2d9")).thenReturn(Optional.ofNullable(employee));
    when(employeeRepository.update("6020e21aaf6f7e7aa0b4a2d9", employeeRequestBody)).thenReturn(employee);
    employeeService.update("6020e21aaf6f7e7aa0b4a2d9", employeeRequestBody);
    verify(employeeRepository, times(1)).update("6020e21aaf6f7e7aa0b4a2d9", employeeRequestBody);
  }

  @Test
  public void testUpdate_WhenEmployeeIsNotPresent() {
    Optional<Employee> employee = Optional.empty();
    when(employeeRepository.findById("6020e21aaf6f7e7aa0b4a2d9")).thenReturn(employee);
    verify(employeeRepository, times(0)).update("6020e21aaf6f7e7aa0b4a2d9", employeeRequestBody);

  }


  @Before
  public void setUp() {
    employee = new Employee();
    employee.setId("6020e21aaf6f7e7aa0b4a2d9");
    employee.setFirstName("abhi");
    employee.setLastName("para");
    employee.setManager(employee);
    employee.setSalary(1000);
    employee.setEmployeesUnderManagers(new ArrayList<>());
    employeeRequestBody = new EmployeeRequestBody("abhi", "para", ID, null);

    employeeException = new Employee();
    employeeException.setFirstName("Please enter proper employeeid");
    employeeException.setLastName("Employee with id 6020e21aaf6f7e7aa0b4a2d8 does not exist");
  }
  //  @After
  //  public void tearDown() {
  //    Mockito.verifyNoMoreInteractions(this.employeeRepository);
  //    Mockito.verifyNoMoreInteractions(this.employeeService);
  //  }

}