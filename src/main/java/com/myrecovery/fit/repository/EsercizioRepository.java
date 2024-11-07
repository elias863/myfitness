package com.myrecovery.fit.repository;

import com.myrecovery.fit.model.Esercizio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EsercizioRepository extends JpaRepository<Esercizio,Long> {
}