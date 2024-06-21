package com.chillin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChillinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChillinApplication.class, args);
    }

}
