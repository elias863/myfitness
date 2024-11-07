package com.myrecovery.fit.controller;

import com.myrecovery.fit.model.Parametri;
import com.myrecovery.fit.service.ParametriService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/parametri")
public class ParametriController {

    private final ParametriService parametriService;


    public ParametriController(ParametriService parametriService) {
        this.parametriService = parametriService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Parametri>> getAllParametri() {
        try {

            List<Parametri> parametri = new ArrayList<Parametri>(parametriService.findAll());
            parametri = parametriService.findAll();

            if (parametri.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(parametri, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Parametri> createParametri(@RequestBody Parametri parametri) {
        try {
            Parametri _parametri = parametriService
                    .save(new Parametri(parametri.getData(), parametri.getPeso(),parametri.getSonno(), parametri.getBpm(), parametri.getOssigenazione()));
            return new ResponseEntity<>(_parametri, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parametri> updateParametri(@PathVariable("id") long id, @RequestBody Parametri parametri) {
        Optional<Parametri> parametriData = parametriService.findById(id);

        if (parametriData.isPresent()) {
            Parametri _parametri = parametriData.get();
            _parametri.setData(parametri.getData());
            _parametri.setPeso(parametri.getPeso());
            _parametri.setSonno(parametri.getSonno());
            _parametri.setBpm(parametri.getBpm());
            _parametri.setOssigenazione(parametri.getOssigenazione());
            return new ResponseEntity<>(parametriService.save(_parametri), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteParametri(@PathVariable("id") long id) {
        try {
            parametriService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<HttpStatus> deleteAllParametri() {
        try {
            parametriService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}