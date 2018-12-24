package com.github.johanfredin.springdataextensions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class VenuehubApplication {

    public static void main(String[] args) {
        SpringApplication.run(VenuehubApplication.class, args);
    }
}
