package org.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.core")
public class Start {

    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }
}
