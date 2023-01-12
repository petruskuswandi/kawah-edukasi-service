package id.kedukasi.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.FAQRequest;
import id.kedukasi.core.request.UpdateFAQRequest;
import id.kedukasi.core.service.FAQService;

@CrossOrigin
@RestController
@RequestMapping("/FAQ")
public class FAQController {
    
    @Autowired
    FAQService service;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> createFAQ(@RequestBody FAQRequest FAQ) {
        return service.createFAQ(FAQ);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getFAQ(
        @RequestParam(name = "limit", defaultValue = "10") Integer limit,
        @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        return service.getFAQ(limit, page);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getFAQById(@RequestParam(name = "id", required = true) Integer id) {
        return service.getFAQById(id);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> updateFAQ(@RequestBody UpdateFAQRequest updateFAQ) {
        return service.updateFAQ(updateFAQ);
    }

    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteFAQ(@RequestParam(name = "id", required = true) Integer id) {
        return service.deleteFAQById(id);
    }

}
