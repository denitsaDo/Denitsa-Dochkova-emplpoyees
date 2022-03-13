package com.example.sirma_task;

import com.example.sirma_task.service.DataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SirmaTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(SirmaTaskApplication.class, args);

        DataService.start();
    }

}
