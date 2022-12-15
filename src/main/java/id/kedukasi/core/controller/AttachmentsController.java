package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.AttachmentsRequest;
import id.kedukasi.core.request.UpdateAttachmentsRequest;
import id.kedukasi.core.service.AttachmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/attachments")
public class AttachmentsController {

    @Autowired
    AttachmentsService service;

    @GetMapping(value = "{/id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getAttachmentsById(@PathVariable("id") Long id) {
        return service.getAttachmentsById(id);
    }
    @PostMapping("/create")
    public ResponseEntity<Result> createAttachments(@Valid @RequestBody AttachmentsRequest attachments) {
        return service.createAttachments(attachments);
    }
    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getAllAttachments() {
        return service.getAllAttachments();
    }
    @PutMapping("/update")
    public ResponseEntity<Result> updateAttachments(@Valid @RequestBody UpdateAttachmentsRequest attachments) {
        return service.updateAttachments(attachments);
    }
    @DeleteMapping(value = "/delete/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteAttachments(@PathVariable("id") Long id) {
        return service.deleteAttachments(id);
    }
}