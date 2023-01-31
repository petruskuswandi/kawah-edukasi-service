package id.kedukasi.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.SyillabusRequest;
import id.kedukasi.core.request.UpdateSyllabusRequest;
import id.kedukasi.core.service.SyillabusService;



@CrossOrigin
@RestController
@RequestMapping("/syillabus")
public class SyillabusController {
    
    @Autowired
    SyillabusService service;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

     private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/create")
    public ResponseEntity<Result> createSyillabus(@Valid @RequestBody SyillabusRequest syillabus){
        return service.createSyllabus(syillabus);

    }

    @PutMapping("/update")
    public ResponseEntity<Result> updateSyillabus(@Valid @RequestBody UpdateSyllabusRequest syilabus){
        return service.updateSyillabus(syilabus);
    }

    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getAllSyillabus(@RequestParam(required = false,name = "search") String search,
                                  @RequestParam(value = "limit",defaultValue = "10") long limit,
                                  @RequestParam(value = "offset",defaultValue = "1") long offset) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllSyillabus(uri,search,limit,offset);
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getSyillabusById(@PathVariable("id") Long id){
        return service.getSyillabusById(id);
    }
    
    @DeleteMapping(value = "/hardDeleted/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> deleteSyillabus(@PathVariable("id") Long id){
        return service.deleteSyillabus(id);
    }

    @PatchMapping(value = "/softDeleted/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> softDeleteSyillabus(@PathVariable("id") Long id) {
        return service.softDeleteSyillabus(id);
    }
}
