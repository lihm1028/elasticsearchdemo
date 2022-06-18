package com.example.esdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EsDemoApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsDemoApplication.class);


    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = SpringApplication.run(EsDemoApplication.class, args);
    }


}
