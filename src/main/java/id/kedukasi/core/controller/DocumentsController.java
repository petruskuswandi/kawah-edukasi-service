package id.kedukasi.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.DocumentsRequest;
import id.kedukasi.core.request.UpdateDocumentsRequest;
import id.kedukasi.core.service.DocumentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/documents")
public class DocumentsController {

    @Autowired
    DocumentsService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/create")
    public ResponseEntity<Result> createDocument(
            @RequestParam("userId") Integer userId,
            @RequestParam("statusId") Integer statusId,
            @RequestPart(value = "file", required = true) MultipartFile multipartFile,
            HttpServletRequest request
            ) {
        return service.createDocument(userId, statusId, multipartFile, request);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getDocumentById(@PathVariable("id") Integer id) {
        return service.getDocumentById(id);
    }

    @PutMapping("/update")
    public ResponseEntity<Result> updateDocuments(
            @RequestParam("documentId") Integer documentId,
            @RequestParam("userId") Integer userId,
            @RequestParam("statusId") Integer statusId,
            @RequestPart(value = "file", required = true) MultipartFile multipartFile,
            HttpServletRequest request
    ) {

        return service.updateDocuments(documentId, userId, statusId, multipartFile, request);
    }

    @GetMapping(value = "/user/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getDocumentsByUserId(@PathVariable("id") Long id) {
        return service.getDocumentByUserId(id);
    }

    @PatchMapping(value = "/delete/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteDocuments(@PathVariable("id") Integer id) {
        return service.deleteDocuments(id);
    }
}
