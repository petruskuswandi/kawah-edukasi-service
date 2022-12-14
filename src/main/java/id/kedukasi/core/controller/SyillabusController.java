package id.kedukasi.core.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.SyillabusRequest;
import id.kedukasi.core.request.UpdateSyllabusRequest;
import id.kedukasi.core.service.SyillabusService;



@CrossOrigin
@RestController
@RequestMapping("/syillabus")
public class SyillabusController {
    
    @Autowired
    SyillabusService service;

    // private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/create")
    public ResponseEntity<Result> createSyillabus(@Valid @RequestBody SyillabusRequest syillabus){
        return service.createSyllabus(syillabus);

    }

    @PutMapping("/update")
    public ResponseEntity<Result> updateSyillabus(@Valid @RequestBody UpdateSyllabusRequest syilabus){
        return service.updateSyillabus(syilabus);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteSyillabus(@PathVariable("id") Long id){
        return service.deleteSyillabus(id);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getAllSyillabus() {
        return service.getAllSyillabus();
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getSyillabusById(@PathVariable("id") Long id){
        return service.getSyillabusById(id);
    }

}
