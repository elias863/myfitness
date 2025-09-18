package com.myrecovery.fit.controller;

import com.myrecovery.fit.model.Esercizio;
import com.myrecovery.fit.service.EsercizioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/esercizi")
public class EsercizioController {

    private final EsercizioService esercizioService;

    public EsercizioController(EsercizioService esercizioService) {
        this.esercizioService = esercizioService;
    }

    @GetMapping("/all")
    public List<Esercizio> getAllEsercizi() {
        List<Esercizio> esercizi = new ArrayList<Esercizio>(esercizioService.findAll());
        esercizi = esercizioService.findAll();

        return esercizi;
    }

    @PostMapping("/create")
    public void createEsercizio(@RequestBody Esercizio esercizio) {

        esercizioService.save(new Esercizio(esercizio.getData(), esercizio.getTipo(), esercizio.getDurata(), esercizio.getCalorie(), esercizio.getDistanza()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Esercizio> updateEsercizio(@PathVariable("id") long id, @RequestBody Esercizio esercizio) {
        Optional<Esercizio> eserciziData = esercizioService.findById(id);

        if (eserciziData.isPresent()) {
            Esercizio _esercizio = eserciziData.get();
            _esercizio.setData(esercizio.getData());
            _esercizio.setTipo(esercizio.getTipo());
            _esercizio.setDurata(esercizio.getDurata());
            _esercizio.setCalorie(esercizio.getCalorie());
            _esercizio.setDurata(esercizio.getDurata());
            return new ResponseEntity<>(esercizioService.save(_esercizio), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteEsercizio(@PathVariable("id") long id) {
        try {
            esercizioService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteall")
    public ResponseEntity<HttpStatus> deleteAllEsercizi() {
        try {
            esercizioService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}