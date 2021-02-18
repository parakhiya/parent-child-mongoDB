package com.example.parentChildMongo.repository;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.parentChildMongo.entity.Employee;
import com.example.parentChildMongo.entity.EmployeeRequestBody;

import de.flapdoodle.embed.mongo.MongodExecutable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class EmployeeRepositoryTest {
  private MongodExecutable mongodExecutable;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Before
  public void setUp() throws Exception {
    Employee employee1 = new Employee("602b6bc61b6ac42af4e43db8", "nayan", "jadeja", 10000, null, null);
    Employee employee2 = new Employee("602b6bdd1b6ac42af4e43db9", "phani", "p.", 10000, null, null);
    Employee employee3 = new Employee("602b6bee1b6ac42af4e43dba", "abhi", "para", 10000, employee2, null);
    mongoTemplate.save(employee1);
    mongoTemplate.save(employee2);
    mongoTemplate.save(employee3);
  }

  @After
  public void tearDown() {
    employeeRepository.deleteAll();
  }

  @Test
  public void testGetBySalary() {
    List<Employee> employees = employeeRepository.getBySalary(1000);
    Assert.assertEquals(3, employees.size());
  }

  @Test
  public void testUpdate() {
    EmployeeRequestBody employee =
        EmployeeRequestBody.builder().firstName("abhi2").lastName("para2").salary(10000).build();
    Employee employeeUpdated = employeeRepository.update("602b6bee1b6ac42af4e43dba", employee);
    Assert.assertEquals(employeeUpdated.getFirstName(), "abhi2");
    Assert.assertEquals(employeeUpdated.getLastName(), "para2");
  }
}