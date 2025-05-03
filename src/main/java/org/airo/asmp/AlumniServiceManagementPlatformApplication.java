package org.airo.asmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "org.airo.asmp.model")
public class AlumniServiceManagementPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlumniServiceManagementPlatformApplication.class, args);
	}

}
