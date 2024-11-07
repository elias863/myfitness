package com.myrecovery.fit.service;

import com.myrecovery.fit.model.Parametri;
import com.myrecovery.fit.repository.ParametriRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParametriService {

    private final ParametriRepository parametriRepository;

    public ParametriService(ParametriRepository parametriRepository) {
        this.parametriRepository = parametriRepository;
    }

    public List<Parametri> findAll(){
        return parametriRepository.findAll();
    }

    public Optional<Parametri> findById(Long id){
        return parametriRepository.findById(id);
    }

    public Parametri save(Parametri parametri){
        return parametriRepository.save(parametri);
    }

    public void deleteById(Long id){
        parametriRepository.deleteById(id);
    }

    public void deleteAll(){
        parametriRepository.deleteAll();
    }
}