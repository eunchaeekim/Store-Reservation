package com.example.mentoringproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MentoringProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MentoringProjectApplication.class, args);
    }

}
