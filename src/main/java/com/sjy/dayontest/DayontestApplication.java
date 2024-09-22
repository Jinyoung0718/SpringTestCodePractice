package com.sjy.dayontest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class DayontestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DayontestApplication.class, args);
    }

}
