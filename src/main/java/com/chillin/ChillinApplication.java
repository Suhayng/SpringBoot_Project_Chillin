package com.chillin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling   // 스프링 스케줄러
public class ChillinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChillinApplication.class, args);
    }

}
