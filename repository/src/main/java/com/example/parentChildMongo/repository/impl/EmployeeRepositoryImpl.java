package com.example.parentChildMongo.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.example.parentChildMongo.entity.Employee;
import com.example.parentChildMongo.entity.EmployeeRequestBody;
import com.example.parentChildMongo.repository.EmployeeRepository;
import com.example.parentChildMongo.repository.EmployeeRepositoryCustom;


public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private EmployeeRepository employeeRepository;

  private Employee createEmployeeFromEmployeeRequestResponseBody(EmployeeRequestBody employeeRequestBody,
      String employeeId) {
    Employee employee = new Employee();
    employee.setId(employeeId);
    employee.setFirstName(employeeRequestBody.getFirstName());
    employee.setLastName(employeeRequestBody.getLastName());
    employee.setSalary(employeeRequestBody.getSalary());
    employee.setEmployeesUnderManagers(new ArrayList<>());
    return employee;
  }

  @Override
  public List<Employee> getBySalary(int salary) {
    //name should match the written query
    Query query = new Query(Criteria.where("salary").is(salary));
    return mongoTemplate.find(query, Employee.class);
  }

  @Override
  public Employee update(String employeeId, EmployeeRequestBody employeeRequestBody) {
    return mongoTemplate
        .save(createEmployeeFromEmployeeRequestResponseBody(employeeRequestBody, employeeId), "employee");
  }
}
