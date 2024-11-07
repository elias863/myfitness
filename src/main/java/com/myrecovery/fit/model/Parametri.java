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
@Table(name = "parametri")
public class Parametri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private Float peso;

    @Column(nullable = false)
    private LocalTime sonno;

    @Column(nullable = false)
    private Integer bpm;

    @Column(nullable = false)
    private Integer ossigenazione;

    // Costruttore con argomenti
    public Parametri(LocalDate data, Float peso, LocalTime sonno, Integer bpm, Integer ossigenazione) {
        this.data = data;
        this.peso = peso;
        this.sonno = sonno;
        this.bpm = bpm;
        this.ossigenazione = ossigenazione;
    }
}