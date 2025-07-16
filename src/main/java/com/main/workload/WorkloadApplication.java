package com.main.workload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class WorkloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkloadApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NonNull CorsRegistry registry) {
				registry
						.addMapping("/api")
						.allowedOrigins("*")
						.allowedMethods("*");
				registry
						.addMapping("/error")
						.allowedOrigins("*")
						.allowedMethods("*");
			}
		};
	}

}
