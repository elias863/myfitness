package com.myrecovery.fit.service;

import com.myrecovery.fit.model.Esercizio;
import com.myrecovery.fit.repository.EsercizioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EsercizioService {

    private final EsercizioRepository esercizioRepository;

    public EsercizioService(EsercizioRepository esercizioRepository) {
        this.esercizioRepository = esercizioRepository;
    }

    public List<Esercizio> findAll() {
        return esercizioRepository.findAll();
    }

    public Optional<Esercizio> findById(Long id) {
        return esercizioRepository.findById(id);
    }

    public Esercizio save(Esercizio esercizio) {
        return esercizioRepository.save(esercizio);
    }

    public void deleteById(Long id) {
        esercizioRepository.deleteById(id);
    }

    public void deleteAll(){
        esercizioRepository.deleteAll();
    }
}