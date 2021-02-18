package com.example.parentChildMongo.constant;

public interface Constant {
  String PARENT_CHILD = "/parentChild";
  String GET_ALL_EMPLOYEES = "/getAllEmployees/{pageNumber}";
  String CREATE_EMPLOYEE = "/employee";
  String DELETE_BY_ID_EMPLOYEE = "/deleteByIdEmployee/{employeeIdOld}/{employeeIdNew}";
  String DELETE_BY_ID_EMPLOYEE_NOT_MANAGER = "/deleteByIdEmployee/{employeeIdOld}";
  String FIND_BY_ID_EMPLOYEE = "/findByIdEmployee/{employeeId}";
  String FIND_HIERARCHY = "/findHierarchy/{employeeId}";
  String GET_BY_SALARY = "/getBySalary/{salary}";
  String UPDATE = "/update/{employeeId}";
  String ID = "/6032127099ad584610d469ef";
  String ID1 = "/6032127099ad584610d469e8";
}
