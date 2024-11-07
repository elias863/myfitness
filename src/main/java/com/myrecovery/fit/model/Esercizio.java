package com.myrecovery.fit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@ToString
@Data
@Entity
@Table(name = "esercizi")
public class Esercizio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false)
    private LocalTime durata;

    @Column(nullable = false)
    private Integer calorie;

    @Column
    private Float distanza;

    // Costruttore
    public Esercizio(LocalDate data, String tipo, LocalTime durata, Integer calorie, Float distanza) {
        this.data = data;
        this.tipo = tipo;
        this.durata = durata;
        this.calorie = calorie;
        this.distanza = distanza;
    }
}