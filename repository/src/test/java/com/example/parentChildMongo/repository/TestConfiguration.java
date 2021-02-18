package com.example.parentChildMongo.repository;

import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = {"com.example.parentChildMongo.repository"})
@EnableMongoAuditing
public class TestConfiguration extends AbstractMongoClientConfiguration {
  @Override
  protected String getDatabaseName() {
    return "employee-fake";
  }

  @Override
  public MongoClient mongoClient() {
    ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/employee-fake");
    MongoClientSettings mongoClientSettings =
        MongoClientSettings.builder().applyConnectionString(connectionString).build();

    return MongoClients.create(mongoClientSettings);
  }

  @Override
  public Collection getMappingBasePackages() {
    return Collections.singleton("com.example.parentChildMongo");
  }
}
