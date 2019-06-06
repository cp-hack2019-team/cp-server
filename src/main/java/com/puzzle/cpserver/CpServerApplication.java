package com.puzzle.cpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


import com.puzzle.dao.config.DaoConfiguration;

@SpringBootApplication
@Import(DaoConfiguration.class)
@ComponentScan("com.puzzle.controller")
public class CpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CpServerApplication.class, args);
    }

}
