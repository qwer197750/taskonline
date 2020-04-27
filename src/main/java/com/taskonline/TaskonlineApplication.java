package com.taskonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class TaskonlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskonlineApplication.class, args);
    }

}
