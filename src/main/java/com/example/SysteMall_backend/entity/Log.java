package com.example.SysteMall_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "log") // Renomeado para evitar conflito
@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Alterado para 'id' para seguir convenção Java

    private String descricao;

    private LocalDateTime dataHora;

    // Construtor padrão
    public Log() {}

    // Construtor com parâmetros
    public Log(String descricao) {
        this.descricao = descricao;
        this.dataHora = LocalDateTime.now(); // Data e hora da criação do log
    }
}
