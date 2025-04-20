package com.remitroserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RemitroServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RemitroServerApplication.class, args);
	}
}
