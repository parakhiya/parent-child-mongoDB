package com.example.parentChildMongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ParentChildMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParentChildMongoApplication.class, args);
	}

}
