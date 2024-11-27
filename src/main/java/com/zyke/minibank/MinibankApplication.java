package com.zyke.minibank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MinibankApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinibankApplication.class, args);
	}

}
