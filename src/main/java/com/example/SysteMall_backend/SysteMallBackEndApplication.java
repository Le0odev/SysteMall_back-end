package com.example.SysteMall_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class SysteMallBackEndApplication {

    @PostConstruct
    public void init() {
        // Define o fuso horário com -3 horas de diferença em relação ao padrão UTC
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
        System.out.println("Fuso horário definido para GMT-3");
    }

    public static void main(String[] args) {
        SpringApplication.run(SysteMallBackEndApplication.class, args);
    }
}
