package com.sahil.campus_event_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CampusEventSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampusEventSystemApplication.class, args);
		System.out.println("DB_PASSWORD=" + System.getenv("DB_PASSWORD"));

	}

}
