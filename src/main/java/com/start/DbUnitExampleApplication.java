package com.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackages="com.example")
@ImportResource({"classpath:applicationContext.xml"})
public class DbUnitExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbUnitExampleApplication.class, args);
	}
}
