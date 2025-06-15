package com.saerok.showing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ShowingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShowingApplication.class, args);
    }
}
