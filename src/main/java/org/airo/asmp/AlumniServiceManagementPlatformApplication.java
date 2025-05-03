package org.airo.asmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "校友服务管理平台 API", version = "1.0", description = "API 文档"))
@EntityScan(basePackages = "org.airo.asmp.model")
public class AlumniServiceManagementPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlumniServiceManagementPlatformApplication.class, args);
	}

}
