package com.example.SysteMall_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class SysteMallBackEndApplication {

    @PostConstruct
    public void init() {
        // Define o fuso horário padrão como "America/Sao_Paulo"
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        System.out.println("Fuso horário definido para: " + TimeZone.getDefault().getID());
    }

    public static void main(String[] args) {
        SpringApplication.run(SysteMallBackEndApplication.class, args);
    }
}
