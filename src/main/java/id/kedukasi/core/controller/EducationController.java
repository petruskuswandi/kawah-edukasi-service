package id.kedukasi.core.controller;


import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.SaveEducationRequest;
import id.kedukasi.core.request.UpdateEducationRequest;
import id.kedukasi.core.service.EducationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/education")
public class EducationController {
    @Autowired
    private EducationService educationService;


    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> createEducation(@RequestBody SaveEducationRequest request) {
        log.info("education/create - {},{}", request.getName(),request.getDescription());

        return educationService.saveEducation(request);
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> updateEducation(@RequestBody UpdateEducationRequest request, @PathVariable Integer id) {
        log.info("education/update - id : {}", id);

        return educationService.updateEducation(request, id);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getEducationById(@PathVariable Integer id) {
        log.info("education/get - id : {}", id);

        return educationService.getById(id);
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getAllEducation() {
        log.info("education/getAll -");

        return educationService.getAll();
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteeducation(@PathVariable Integer id) {
        log.info("education/delete - id : {}", id);

        return educationService.deleteEducation(id);
    }


}
