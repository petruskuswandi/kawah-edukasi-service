package id.kedukasi.core.controller;

import javax.validation.Valid;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.DocumentsRequest;
import id.kedukasi.core.request.UpdateDocumentsRequest;
import id.kedukasi.core.service.DocumentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/documents")
public class DocumentsController {

    @Autowired
    DocumentsService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/create")
    public ResponseEntity<Result> createDocument(@Valid @RequestBody DocumentsRequest document) {
        return service.createDocument(document);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getDocumentById(@PathVariable("id") Integer id) {
        return service.getDocumentById(id);
    }

    @PutMapping("/update")
    public ResponseEntity<Result> updateDocuments(@Valid @RequestBody UpdateDocumentsRequest documents) {
        return service.updateDocuments(documents);
    }

    @GetMapping(value = "/user/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getDocumentsByUserId(@PathVariable("id") Long id) {
        return service.getDocumentByUserId(id);
    }

    @DeleteMapping(value = "/delete/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteDocuments(@PathVariable("id") Integer id) {
        return service.deleteDocuments(id);
    }
}
