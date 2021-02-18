package com.example.parentChildMongo.service;

import java.util.List;

import com.example.parentChildMongo.entity.Employee;
import com.example.parentChildMongo.entity.EmployeeRequestBody;
import com.example.parentChildMongo.entity.EmployeeResponseBody;
import com.example.parentChildMongo.entity.Response;

public interface EmployeeService {

  /**
   * getAllEmployees
   * @return
   */
  List<EmployeeResponseBody> getAllEmployees(int pageNumber);

  /**
   * create
   * @param employee
   * @return
   */
  void create(EmployeeRequestBody employee);


  /**
   * delete
   * @param employeeIdOld
   * @param employeeIdNew
   * @return
   */
  void delete(String employeeIdOld, String employeeIdNew);

  /**
   * find
   * @param employeeId
   * @return
   */
  EmployeeResponseBody find(String employeeId);

  /**
   * findHierarchy
   * @param employeeId
   * @return
   */
  List<EmployeeResponseBody> findHierarchy(String employeeId);

  /**
   * @param salary
   * @return
   */
  List<Employee> getBySalary(int salary);

  /**
   * @param employeeId
   * @param employeeRequestBody
   */
  void update(String employeeId, EmployeeRequestBody employeeRequestBody);
}
