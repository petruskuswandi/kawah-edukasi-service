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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/publicbatch")
public class BatchPublicController {

    @Autowired
    BatchService service;
    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
//    public Result getAll() {
//        String uri = stringUtil.getLogParam(request);
//        logger.info(uri);
//        return service.getAllBatch(uri);
//    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Result getBatchByid(@PathVariable("id") Long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getBatchById(id, uri);
    }

    @GetMapping(value = "/running", produces = APPLICATION_JSON_VALUE)
    public Result getBatchRunning() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllBatchRunning(uri);
    }

//    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
//    public Result getClassBybatch(@PathVariable("id") Long id) {
//        String uri = stringUtil.getLogParam(request);
//        logger.info(uri);
//        return service.getClassBybatch(id, uri);
//    }
////
//    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
//    public Result getMentorClassByBatch(@PathVariable("id") Long id) {
//        String uri = stringUtil.getLogParam(request);
//        logger.info(uri);
//        return service.getMentorClassByBatch(id, uri);
//    }


}
