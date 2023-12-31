package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.StatusRequest;
import id.kedukasi.core.request.UpdateStatusRequest;
import id.kedukasi.core.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/status")
public class StatusController {
    @Autowired
    StatusService service;

    @PostMapping("/create")
    public ResponseEntity<Result> createStatus(@Valid @RequestBody StatusRequest status) {
        return service.createStatus(status);
    }

    @PutMapping("/update")
    public ResponseEntity<Result> updateStatus(@Valid @RequestBody UpdateStatusRequest status) {
        return service.updateStatus(status);
    }

    @DeleteMapping(value = "/delete/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteStatus(@PathVariable("id") int id) {
        return service.deleteStatusById(id);
    }

    @PatchMapping(value = "/delete")
    public ResponseEntity<Result> softDeleteStatus(
        @RequestParam(value = "id", defaultValue = "0", required = true) int id,
        @RequestParam(value = "deleted", defaultValue = "false") boolean deleted
    ) {
        return service.softDeleteStatus(id, deleted);
    }

    // @GetMapping(value = "/get/all", produces = APPLICATION_JSON_VALUE)
    // public ResponseEntity<Result> getAllStatus() {
    //     return service.getAllStatus();
    // }

    @GetMapping(value = "/get/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getStatus(@RequestParam(name = "flag", required = false) String flag,
                                            @RequestParam(name = "sub flag", required = false) String subFlag,
                                            @RequestParam(name = "limit", defaultValue = "10") Integer limit,
                                            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        return service.getStatus(flag, subFlag, limit, page);
    }

    @GetMapping(value = "/get/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getStatusById(@PathVariable("id") int id) {
        return service.getStatusById(id);
    }

}
