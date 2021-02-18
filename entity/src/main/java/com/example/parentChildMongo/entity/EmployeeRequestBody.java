package com.example.parentChildMongo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRequestBody {
  private String firstName;
  private String lastName;
  private String managerId;
  private Integer salary;
}
