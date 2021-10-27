package com.programmers.devcourse.vaemin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VaeminApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaeminApplication.class, args);
	}

}
