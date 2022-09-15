package id.kedukasi.core.controller;


import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.BatchRequest;
import id.kedukasi.core.service.BatchService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/batch")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class BatchController {

    @Autowired
    BatchService service;
    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public Result getAll() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllBatch(uri);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Result getUserByid(@PathVariable("id") long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getBatchById(id, uri);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBatch(@Valid @RequestBody BatchRequest batchRequest) {
        return service.updateBatch(batchRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBatch(@Valid @RequestBody BatchRequest batchRequest) {

        return service.updateBatch(batchRequest);
    }

    @PatchMapping(value = "/delete")
    public ResponseEntity<?> deleteBatch(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id,
            @RequestParam(value = "banned", defaultValue = "true") boolean banned
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.deleteBatch(banned, id, uri);
    }






}

