package com.takarub.AuthJwtTemplate;

import com.takarub.AuthJwtTemplate.dto.AuthenticationResponse;
import com.takarub.AuthJwtTemplate.dto.RegisteredRequest;
import com.takarub.AuthJwtTemplate.model.Role;
import com.takarub.AuthJwtTemplate.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class AuthJwtTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthJwtTemplateApplication.class, args);
	}


//	@Bean
//	public CommandLineRunner commandLineRunner(AuthService authService){
//		return args -> {
//			RegisteredRequest adminRequest = RegisteredRequest.builder()
//					.firstName("Admin")
//					.lastName("User")
//					.email("admin@example.com")
//					.passWord("adminPassword") // Ensure to use a secure password
//					.phoneNumber("1234567890")
//					.role(Role.ADMIN) // Set the role as ADMIN
//					.build();
//			authService.register(adminRequest);
//		};
//	}

}
