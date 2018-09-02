package com.netgloo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IzrApplication {
	//public static String ROOT = "target/classes/static/uploads";
	public static String ROOT = "uploads";

	public static void main(String[] args) {
		SpringApplication.run(IzrApplication.class, args);	
	}
	
}
