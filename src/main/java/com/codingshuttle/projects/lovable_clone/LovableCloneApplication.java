package com.codingshuttle.projects.lovable_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LovableCloneApplication {

	public static void main(String[] args) {
		System.out.println(java.util.TimeZone.getDefault());
		SpringApplication.run(LovableCloneApplication.class, args);
	}

}
