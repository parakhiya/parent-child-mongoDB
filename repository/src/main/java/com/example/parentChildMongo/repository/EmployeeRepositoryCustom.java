package com.example.parentChildMongo.repository;

import java.util.List;

import com.example.parentChildMongo.entity.Employee;
import com.example.parentChildMongo.entity.EmployeeRequestBody;

public interface EmployeeRepositoryCustom {
  /**
   * @param salary
   * @return
   */
  List<Employee> getBySalary(int salary);

  /**
   * @param employeeId
   * @param employeeRequestBody
   */
  Employee update(String employeeId, EmployeeRequestBody employeeRequestBody);

}
