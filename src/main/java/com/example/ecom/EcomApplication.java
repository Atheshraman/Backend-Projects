package com.example.ecom;

import com.example.ecom.Repo.userRepo;
import com.example.ecom.model.users;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcomApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomApplication.class, args);
	}
	@Bean
	CommandLineRunner seedAdmin(userRepo repo, PasswordEncoder passwordEncoder){
		return args->{
			if(!repo.existsByEmail("Admin@gmail.com")){
				users admin=new users();
				admin.setEmail("Admin@gmail.com");
				admin.setPassword(passwordEncoder.encode("password"));
				admin.setUsername("Admin");
				admin.setFirstname("A");
				admin.setLastname("R");
				admin.setRole("ADMIN");
				admin.setPhone("10000");
				admin.setAddress("AdminAddress");
				repo.save(admin);
			}
		};
	}
}
