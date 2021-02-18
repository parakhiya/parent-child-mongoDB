package com.example.parentChildMongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.parentChildMongo.entity.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String>, EmployeeRepositoryCustom {
}
