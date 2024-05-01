package config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = {"controller", "service","dao", "repository","entity","DTO","domain"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan( basePackages = {"entity", "domain"})
public class PhoneNumberValidator {
    public static void main(String[] args) {
        SpringApplication.run(PhoneNumberValidator.class, args);
    }
}
