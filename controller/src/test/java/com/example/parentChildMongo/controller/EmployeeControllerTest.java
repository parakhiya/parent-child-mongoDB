package com.example.parentChildMongo.controller;

import static com.example.parentChildMongo.constant.Constant.CREATE_EMPLOYEE;
import static com.example.parentChildMongo.constant.Constant.ID;
import static com.example.parentChildMongo.constant.Constant.ID1;
import static com.example.parentChildMongo.constant.Constant.PARENT_CHILD;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.parentChildMongo.entity.Employee;
import com.example.parentChildMongo.entity.EmployeeRequestBody;
import com.example.parentChildMongo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @InjectMocks
  private EmployeeController employeeController;
  @Autowired
  private MockMvc mockMvc;
  @Mock
  private EmployeeService employeeService;

  private Employee employee;

  @Test
  public void testFind() throws Exception {
    mockMvc.perform(get(PARENT_CHILD + "/findByIdEmployee" + ID)).andDo(print()).andExpect(status().isOk());
    verify(employeeService).find("6032127099ad584610d469ef");
  }

  @Test
  public void testFind_WhenException() throws Exception {
    doThrow(new RuntimeException()).when(employeeService).find("6032127099ad584610d469ef");
    mockMvc.perform(get(PARENT_CHILD + "/findByIdEmployee" + ID)).andDo(print()).andExpect(status().isOk());

    verify(employeeService).find("6032127099ad584610d469ef");
  }

  @Test
  public void testDeleteByIdEmployee() throws Exception {
    mockMvc.perform(get(PARENT_CHILD + "/deleteByIdEmployee" + ID + ID1)).andDo(print()).andExpect(status().isOk());
    verify(employeeService).delete("6032127099ad584610d469ef", "6032127099ad584610d469e8");
  }

  @Test
  public void testDeleteByIdEmployee_WhenException() throws Exception {
    doThrow(new RuntimeException()).when(employeeService)
        .delete("6032127099ad584610d469ef", "6032127099ad584610d469e8");
    mockMvc.perform(get(PARENT_CHILD + "/deleteByIdEmployee" + ID + ID1)).andDo(print()).andExpect(status().isOk());
    verify(employeeService).delete("6032127099ad584610d469ef", "6032127099ad584610d469e8");
  }

  @Test
  public void testGetAllEmployees() throws Exception {
    mockMvc.perform(get(PARENT_CHILD + "/getAllEmployees" + "/2")).andDo(print()).andExpect(status().isOk());
    verify(employeeService).getAllEmployees(2);
  }

  @Test
  public void testGetAllEmployees_WhenException() throws Exception {
    doThrow(new RuntimeException()).when(employeeService).getAllEmployees(2);
    mockMvc.perform(get(PARENT_CHILD + "/getAllEmployees" + "/2")).andDo(print()).andExpect(status().isOk());
    verify(employeeService).getAllEmployees(2);
  }

  @Test
  public void testFindHierarchy() throws Exception {
    mockMvc.perform(get(PARENT_CHILD + "/findHierarchy" + ID)).andDo(print()).andExpect(status().isOk());
    verify(employeeService).findHierarchy("6032127099ad584610d469ef");
  }

  @Test
  public void testFindHierarchy_WhenException() throws Exception {
    doThrow(new RuntimeException()).when(employeeService).findHierarchy("6032127099ad584610d469ef");
    mockMvc.perform(get(PARENT_CHILD + "/findHierarchy" + ID)).andDo(print()).andExpect(status().isOk());
    verify(employeeService).findHierarchy("6032127099ad584610d469ef");
  }

  @Test
  public void testCreateEmployees() throws Exception {
    EmployeeRequestBody employeeRequestBody = new EmployeeRequestBody("abhi", "para", ID, null);
    mockMvc.perform(post(PARENT_CHILD + CREATE_EMPLOYEE).contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(new ObjectMapper().writeValueAsString(employeeRequestBody))).andDo(print()).andExpect(status().isOk());
    verify(employeeService).create(employeeRequestBody);
  }

  @Test
  public void testCreateEmployees_WhenException() throws Exception {
    EmployeeRequestBody employeeRequestBody = new EmployeeRequestBody("abhi", "para", ID, null);
    doThrow(new RuntimeException()).when(employeeService).create(employeeRequestBody);
    mockMvc.perform(post(PARENT_CHILD + CREATE_EMPLOYEE).contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(new ObjectMapper().writeValueAsString(employeeRequestBody))).andDo(print()).andExpect(status().isOk());
    verify(employeeService).create(employeeRequestBody);
  }

  @Test
  public void testGetBySalary() throws Exception {
    mockMvc.perform(get(PARENT_CHILD + "/getBySalary" + "/1000")).andDo(print()).andExpect(status().isOk());
    verify(employeeService).getBySalary(1000);
  }

  @Test
  public void testGetBySalary_WhenException() throws Exception {
    doThrow(new RuntimeException()).when(employeeService).getBySalary(1000);
    mockMvc.perform(get(PARENT_CHILD + "/getBySalary" + "/1000")).andDo(print()).andExpect(status().isOk());
    verify(employeeService).getBySalary(1000);
  }

  @Test
  public void testUpdate() throws Exception {
    EmployeeRequestBody employeeRequestBody = new EmployeeRequestBody("abhi", "para", null, null);
    mockMvc.perform(put(PARENT_CHILD + "/update" + ID).contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(new ObjectMapper().writeValueAsString(employeeRequestBody))).andDo(print()).andExpect(status().isOk());
    verify(employeeService).update("6032127099ad584610d469ef", employeeRequestBody);
  }

  @Test
  public void testUpdate_WhenException() throws Exception {
    EmployeeRequestBody employeeRequestBody = new EmployeeRequestBody("abhi", "para", null, null);
    doThrow(new RuntimeException()).when(employeeService).update("6032127099ad584610d469ef", employeeRequestBody);
    mockMvc.perform(put(PARENT_CHILD + "/update" + ID).contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(new ObjectMapper().writeValueAsString(employeeRequestBody))).andDo(print()).andExpect(status().isOk());
    verify(employeeService).update("6032127099ad584610d469ef", employeeRequestBody);
  }

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    employee = new Employee();
  }

  @After
  public void tearDown() {
    Mockito.verifyNoMoreInteractions(employeeService);
  }
}