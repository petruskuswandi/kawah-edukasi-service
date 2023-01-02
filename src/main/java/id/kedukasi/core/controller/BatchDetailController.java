package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.batchDetail.BatchDetailRequest;
import id.kedukasi.core.request.batchDetail.UpdateBatchDetailRequest;
import id.kedukasi.core.service.BatchDetailService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/batch-detail")
public class BatchDetailController {

    @Autowired
    BatchDetailService service;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/create")
    public ResponseEntity<Result> createBatchDetail(@Valid @RequestBody BatchDetailRequest batchDetail){
        return service.createBatchDetail(batchDetail);
    }

    @PutMapping("/update")
    public ResponseEntity<Result> updateBatchDetail(@Valid @RequestBody UpdateBatchDetailRequest updateBatchDetailRequest){
        return service.updateBatchDetail(updateBatchDetailRequest);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteBatchDetail(@PathVariable("id") Long id){
        return service.deleteBatchDetail(id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getDetailByIdBatch(@PathVariable("id") Long id){
        return service.getDetailByIdBatch(id);
    }
}
