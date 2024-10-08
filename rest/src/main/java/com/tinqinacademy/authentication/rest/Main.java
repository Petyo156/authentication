package com.tinqinacademy.authentication.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.tinqinacademy.authentication")
@EnableJpaRepositories(basePackages = "com.tinqinacademy.authentication.persistence.repositories")
@EntityScan(basePackages = "com.tinqinacademy.authentication.persistence.entities")
@EnableFeignClients(basePackages = "com.tinqinacademy.authentication.domain")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}