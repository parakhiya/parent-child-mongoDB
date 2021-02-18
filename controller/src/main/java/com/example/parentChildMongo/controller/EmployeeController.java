package com.example.parentChildMongo.controller;

import static com.example.parentChildMongo.constant.Constant.CREATE_EMPLOYEE;
import static com.example.parentChildMongo.constant.Constant.DELETE_BY_ID_EMPLOYEE;
import static com.example.parentChildMongo.constant.Constant.DELETE_BY_ID_EMPLOYEE_NOT_MANAGER;
import static com.example.parentChildMongo.constant.Constant.FIND_BY_ID_EMPLOYEE;
import static com.example.parentChildMongo.constant.Constant.FIND_HIERARCHY;
import static com.example.parentChildMongo.constant.Constant.GET_ALL_EMPLOYEES;
import static com.example.parentChildMongo.constant.Constant.GET_BY_SALARY;
import static com.example.parentChildMongo.constant.Constant.PARENT_CHILD;
import static com.example.parentChildMongo.constant.Constant.UPDATE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parentChildMongo.entity.EmployeeRequestBody;
import com.example.parentChildMongo.entity.GenericResponse;
import com.example.parentChildMongo.entity.Response;
import com.example.parentChildMongo.service.EmployeeService;

//This controller is about Company Employee management
@RestController
@RequestMapping(PARENT_CHILD)
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @PostMapping(value = CREATE_EMPLOYEE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Response create(@RequestBody EmployeeRequestBody employeeRequestBody) {
    try {
      employeeService.create(employeeRequestBody);
      return new Response("Succesfully created new employee", true);
    } catch (Exception e) {
      return new Response(e.getMessage(), false);
    }
  }

  @GetMapping(value = GET_ALL_EMPLOYEES, produces = MediaType.APPLICATION_JSON_VALUE)
  public GenericResponse getEmployees(@PathVariable(value = "pageNumber") int pageNumber) {
    try {
      return new GenericResponse(employeeService.getAllEmployees(pageNumber));
    } catch (Exception e) {
      Response response = new Response(e.getMessage(), false);
      return new GenericResponse(response);
    }
  }

  @GetMapping(value = FIND_BY_ID_EMPLOYEE, produces = MediaType.APPLICATION_JSON_VALUE)
  public GenericResponse find(@PathVariable(value = "employeeId") String employeeId) {
    try {
      return new GenericResponse(employeeService.find(employeeId));
    } catch (Exception e) {
      return new GenericResponse(new Response(e.getMessage(), false));
    }
  }

  @GetMapping(value = FIND_HIERARCHY, produces = MediaType.APPLICATION_JSON_VALUE)
  public GenericResponse findHierarchy(@PathVariable(value = "employeeId") String employeeId) {
    try {
      return new GenericResponse<>(employeeService.findHierarchy(employeeId));
    } catch (Exception e) {
      Response response = new Response(e.getMessage(), false);
      return new GenericResponse<>(response);
    }
  }

  @GetMapping(value = {DELETE_BY_ID_EMPLOYEE,
      DELETE_BY_ID_EMPLOYEE_NOT_MANAGER}, produces = MediaType.APPLICATION_JSON_VALUE)
  public Response delete(@PathVariable(value = "employeeIdOld") String employeeIdOld,
      @PathVariable(value = "employeeIdNew", required = false) String employeeIdNew) {
    try {
      employeeService.delete(employeeIdOld, employeeIdNew);
      return new Response("Succesfully deleted employee", true);

    } catch (Exception e) {
      return new Response(e.getMessage(), false);
    }
  }

  @GetMapping(value = {GET_BY_SALARY}, produces = MediaType.APPLICATION_JSON_VALUE)
  public GenericResponse getBySalary(@PathVariable(value = "salary") int salary) {
    try {
      return new GenericResponse<>(employeeService.getBySalary(salary));
    } catch (Exception e) {
      return new GenericResponse(new Response(e.getMessage(), false));
    }
  }

  @PutMapping(value = {UPDATE}, produces = MediaType.APPLICATION_JSON_VALUE)
  public Response update(@PathVariable(value = "employeeId") String employeeId,
      @RequestBody EmployeeRequestBody employeeRequestBody) {
    try {
      employeeService.update(employeeId, employeeRequestBody);
      return new Response("Succesfully updated employee", true);
    } catch (Exception e) {
      return new Response(e.getMessage(), false);
    }
  }
}
