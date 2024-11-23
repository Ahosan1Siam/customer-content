package com.assessment.consumer_content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ConsumerContentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerContentApplication.class, args);
	}

}
