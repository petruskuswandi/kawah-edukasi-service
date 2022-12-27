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
import id.kedukasi.core.request.SyillabusDetailRequest;
import id.kedukasi.core.request.UpdateSyillabusDetailRequest;
import id.kedukasi.core.request.UpdateSyllabusRequest;
import id.kedukasi.core.service.SyillabusDetailService;


@CrossOrigin
@RestController
@RequestMapping("/syillabus-detail")
public class SyillabusDetailController {
    
    @Autowired
    SyillabusDetailService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/")
    public ResponseEntity<Result> createSyillabusDetail(@Valid @RequestBody SyillabusDetailRequest syillabusDetail){
        return service.createSyillabusDetail(syillabusDetail);
    }
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getAllSyillabusDetail(){
        return service.getAllSyillabusDetail();
    }

    @PutMapping("/")
    public ResponseEntity<Result> updateSyillabusDetail(@Valid @RequestBody UpdateSyillabusDetailRequest updateSyillabusDetailRequest){
        return service.updateSyillabusDetail(updateSyillabusDetailRequest);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteSyillabusDetail(@PathVariable("id") Long id){
        return service.deleteSyillabusDetail(id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getSyillabusDetailById(@PathVariable("id") Long id){
        return service.getSyillabusDetailById(id);
    }
}
