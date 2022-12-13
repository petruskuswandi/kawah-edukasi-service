package id.kedukasi.core.controller;


import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.TypeDocumentsRequest;
import id.kedukasi.core.request.UpdateTypeRequest;
import id.kedukasi.core.service.TypeDocumentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@CrossOrigin
@RestController
@RequestMapping("/TypeDocuments")
public class TypeDocumentsController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TypeDocumentsService typeDocumentsService;


    @PostMapping("/create")
    public ResponseEntity<Result> createTypeDocument(@Valid @RequestBody TypeDocumentsRequest type) {
        return typeDocumentsService.createTypeDocument(type);
    }

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getAlltypeDocument() {
        return typeDocumentsService.getAllType();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getTypeDocumentId(@PathVariable("id") int id) {
        return typeDocumentsService.getTypeById(id);
    }

    @PutMapping("/update")
    public ResponseEntity<Result> updateTypeDocument(@Valid @RequestBody UpdateTypeRequest type) {
        return typeDocumentsService.updateTypeDocument(type);
    }

    @DeleteMapping(value = "/delete/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteTypeDocument(@PathVariable("id") int id) {
        return typeDocumentsService.deleteTypeDocument(id);
    }

}