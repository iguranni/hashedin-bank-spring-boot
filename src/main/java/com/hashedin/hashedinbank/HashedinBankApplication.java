package com.hashedin.hashedinbank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@Slf4j
@EnableWebSecurity(debug = true)
//@OpenAPIDefinition(info = @Info(title = "Hashedin Bank API", version = "2.0", description = "Bank Information"))
public class HashedinBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(HashedinBankApplication.class, args);
    }
}