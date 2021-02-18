package com.example.parentChildMongo.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "employee")
@Builder
public class Employee {
  @Id
  private String id;

  @Field(value = "firstName")
  private String firstName;

  @Field(value = "lastName")
  private String lastName;

  @Field(value = "salary")
  private Integer salary;

  //why to string exclude because of infinite recursion
  @ToString.Exclude
  @JsonIgnore
  @Field(value = "managerId")
  @DBRef(lazy = true)
  private Employee manager;

  @ToString.Exclude
  @JsonIgnore
  @Field(value = "employeesUnderManagers")
  @DBRef(lazy = true)
  private List<Employee> employeesUnderManagers = new ArrayList<>();

  //if employee has birthdate then make it transient so it will be persistent across database
  //@transient @createeddate @astmodifieddate @nonnull  @Indexed(unique = true)
}
