package com.example.parentChildMongo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.example.parentChildMongo.entity.Employee;
import com.example.parentChildMongo.entity.EmployeeRequestBody;
import com.example.parentChildMongo.entity.EmployeeResponseBody;
import com.example.parentChildMongo.entity.Response;
import com.example.parentChildMongo.repository.EmployeeRepository;
import com.example.parentChildMongo.service.EmployeeService;


@Service
public class EmployeeServiceImpl implements EmployeeService {


  @Autowired
  private EmployeeRepository employeeRepository;


  private Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

  private Response createResponse(String message, boolean success) {
    Response response = new Response();
    response.setMessage(message);
    response.setSuccess(success);
    return response;
  }

  private Employee createEmployeeFromEmployeeRequestBody(EmployeeRequestBody employeeRequestBody) {
    Employee employee = new Employee();
    employee.setFirstName(employeeRequestBody.getFirstName());
    employee.setLastName(employeeRequestBody.getLastName());
    if (!(employeeRequestBody.getManagerId() == null))
      employee.setManager(employeeRepository.findById(employeeRequestBody.getManagerId()).get());
    employee.setSalary(employeeRequestBody.getSalary());
    employee.setEmployeesUnderManagers(new ArrayList<>());
    return employee;
  }

  private EmployeeResponseBody createResponseBody(Employee employee) {
    EmployeeResponseBody employeeResponseBody = new EmployeeResponseBody();
    employeeResponseBody.setFirstName(employee.getFirstName());
    employeeResponseBody.setLastName(employee.getLastName());
    if ((employee.getManager() != null))
      employeeResponseBody.setMaanagerName(employee.getManager().getFirstName());

    employeeResponseBody.setSalary(employee.getSalary());
    return employeeResponseBody;
  }

  @Override
  public List<EmployeeResponseBody> getAllEmployees(int pageNumber) {
    logger.info("All Employees has been called");
    Pageable pageable = PageRequest.of(pageNumber, 10);
    //total pages and page metadata
    Page<Employee> employees = employeeRepository.findAll(pageable);
    logger.info(employees.toString());
    List<EmployeeResponseBody> employeeResponseBodyList = new ArrayList<>();
    //why stream because it is faster than for loop
    employees.stream().forEach(employee -> employeeResponseBodyList.add(createResponseBody(employee)));
    return employeeResponseBodyList;
  }

  @Override
  public void create(EmployeeRequestBody employeeRequestBody) {
    String managerId = employeeRequestBody.getManagerId();
    if (managerId == null) {
      Employee employeeNew = createEmployeeFromEmployeeRequestBody(employeeRequestBody);
      Employee employeeSave = employeeRepository.save(employeeNew);
      logger.info("Created Employee {}", employeeRequestBody.toString());
      return;
    }
    Optional<Employee> managerTemp = employeeRepository.findById(managerId);
    if (!managerTemp.isPresent()) {
      logger.error("manager with this id does not exist");
      throw new ResourceNotFoundException("Manager with id " + managerId + " does not exist");
    }
    Employee manager = managerTemp.get();
    List<Employee> employees = manager.getEmployeesUnderManagers();
    //tie Manager to Employee
    Employee employeeNew = createEmployeeFromEmployeeRequestBody(employeeRequestBody);
    Employee employeeSave = employeeRepository.save(employeeNew);

    //tie Employee to manager
    employees.add(employeeSave);
    employeeRepository.save(manager);
    logger.info("Created Employee {}", employeeSave.toString());
  }

  @Caching(evict = {@CacheEvict(key = "#employeeIdOld", value = "findEmployee"),
      @CacheEvict(key = "#employeeIdOld", value = "findHierarchy")})
  @Override
  public void delete(String employeeIdOld, String employeeIdNew) {
    if (employeeIdOld == null) {
      logger.error("employee with this id does not exist");
      throw new ResourceNotFoundException("Employee with id " + employeeIdNew + " does not exist");
    }
    Optional<Employee> employeeTempOld = employeeRepository.findById(employeeIdOld);
    if (!employeeTempOld.isPresent()) {
      logger.error("employee with this id does not exist");
      throw new ResourceNotFoundException("Employee with id " + employeeIdOld + " does not exist");
    } else if (employeeIdNew != null && !employeeRepository.findById(employeeIdNew).isPresent()) {
      logger.error("employee with this id does not exist");
      throw new ResourceNotFoundException("Employee with id " + employeeIdNew + " does not exist");
    }

    Employee employee = employeeTempOld.get();
    Employee manager = employee.getManager();
    List<Employee> managerSet = manager.getEmployeesUnderManagers();
    managerSet.remove(employeeIdOld);
    manager.setEmployeesUnderManagers(managerSet);
    employeeRepository.save(manager);

    if (employeeIdNew != null) {
      Employee managerNew = employeeRepository.findById(employeeIdNew).get();
      List<Employee> managerSetNew = managerNew.getEmployeesUnderManagers();
      managerSetNew.addAll(employee.getEmployeesUnderManagers());
      managerNew.setEmployeesUnderManagers(managerSetNew);
      employeeRepository.save(managerNew);
    }
    logger.info("deleted Employee with id {}", employeeIdOld);
    employeeRepository.deleteById(employeeIdOld);
  }

  @Cacheable(key = "#employeeId", value = "findEmployee")
  @Override
  public EmployeeResponseBody find(String employeeId) {
    if (employeeId == null) {
      throw new ResourceNotFoundException("Employee with id " + employeeId + " does not exist");
    }
    logger.info("hello i am in service ");
    Optional<Employee> employee = employeeRepository.findById(employeeId);

    if (!employee.isPresent()) {
      logger.error("employee with this id does not exist");
      throw new ResourceNotFoundException("Employee with id " + employeeId + " does not exist");
    }
    logger.info("Find method Employee with id {}", employeeId);
    return createResponseBody(employee.get());
  }

  @Cacheable(key = "#employeeId", value = "findHierarchy")
  @Override
  public List<EmployeeResponseBody> findHierarchy(String employeeId) {
    if (employeeId == null) {
      logger.error("employee with this id does not exist");
      throw new ResourceNotFoundException("Employee with id " + employeeId + " does not exist");
    }
    Optional<Employee> employeeTemp = employeeRepository.findById(employeeId);

    if (!employeeTemp.isPresent()) {
      logger.error("employee with this id does not exist");
      throw new ResourceNotFoundException("Employee with id " + employeeId + " does not exist");
    }

    boolean flag = true;
    Employee employee = employeeTemp.get();
    Employee manager = employee.getManager();
    List<Employee> managers = new ArrayList<>();
    managers.add(manager);
    //optimize while loop
    while (flag) {
      if (manager.getManager() == null) {
        break;
      } else {
        manager = manager.getManager();
        managers.add(manager);
      }
    }
    logger.info("Find Hierarchy is called {}", managers.toString());
    List<EmployeeResponseBody> allEmployee = new ArrayList<>();
    for (Employee employeeInList : managers) {
      allEmployee.add(createResponseBody(employeeInList));
    }
    return allEmployee;
  }

  @Override
  public List<Employee> getBySalary(int salary) {
    return employeeRepository.getBySalary(salary);
  }

  @Caching(evict = {@CacheEvict(key = "#employeeIdOld", value = "findEmployee"),
      @CacheEvict(key = "#employeeIdOld", value = "findHierarchy")})
  @Override
  public void update(String employeeId, EmployeeRequestBody employeeRequestBody) {
    if (employeeId == null || !employeeRepository.findById(employeeId).isPresent()) {
      logger.error("employee with this id does not exist");
      throw new ResourceNotFoundException("Employee with id " + employeeId + " does not exist");
    }
    employeeRepository.update(employeeId, employeeRequestBody);
  }
}