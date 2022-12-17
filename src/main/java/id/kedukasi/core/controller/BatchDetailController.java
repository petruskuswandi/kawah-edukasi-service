package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.BatchDetailRequest;
import id.kedukasi.core.request.UpdateBatchDetailRequest;
import id.kedukasi.core.service.BatchDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/batch-detail")
public class BatchDetailController {

    @Autowired
    BatchDetailService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/")
    public ResponseEntity<Result> createBatchDetail(@Valid @RequestBody BatchDetailRequest batchDetail){
        return service.createBatchDetail(batchDetail);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getAllBatchDetail() {
        return service.getAllBatchDetail();
    }

    @PutMapping("/")
    public ResponseEntity<Result> updateBatchDetail(@Valid @RequestBody UpdateBatchDetailRequest updateBatchDetailRequest){
        return service.updateBatchDetail(updateBatchDetailRequest);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteBatchDetail(@PathVariable("id") Long id){
        return service.deleteBatchDetail(id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getBatchDetailId(@PathVariable("id") Long id){
        return service.getBatchDetailId(id);
    }

}
