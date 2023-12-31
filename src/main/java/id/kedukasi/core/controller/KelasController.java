package id.kedukasi.core.controller;

import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.request.KelasRequest;
import id.kedukasi.core.request.UpdateKelasRequest;
import id.kedukasi.core.service.KelasService;
import id.kedukasi.core.serviceImpl.KelasServiceImpl;
import id.kedukasi.core.utils.StringUtil;
import io.swagger.annotations.ApiResponse;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/kelas")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class KelasController {

    @Autowired

    KelasService service;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private KelasRepository kelasRepository;

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public Result getAll(@RequestParam(required = false,name = "search") String search,
                         @RequestParam(value = "limit",defaultValue = "10") long limit,
                         @RequestParam(value = "offset",defaultValue = "1") long offset){
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllClass(uri,search,limit,offset);
    }

    @GetMapping(value = "/allBanned", produces = APPLICATION_JSON_VALUE)
    public Result getAllBannedKelas() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllBannedKelas(uri);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Result getClassByid(@PathVariable("id") Long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getClassById(id, uri);
    }

//    @GetMapping(value = "/batch/{id}",produces = APPLICATION_JSON_VALUE)
//    public Result getBatchByKelasId(@PathVariable("id") long id){
//        String uri = stringUtil.getLogParam(request);
//        logger.info(uri);
//        return service.getAllBatchByKelas(id);
//    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createClass(@Valid @RequestBody KelasRequest Request) {
        return service.createClass(Request);
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<Result> updateClass(@Valid @RequestBody UpdateKelasRequest kelasRequest) {
        return service.updateClass(kelasRequest);
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/delete")
    public ResponseEntity<?> deleteClass(
            @RequestParam(value = "id", defaultValue = "0", required = true) Long id,
            @RequestParam(value = "banned", defaultValue = "true") boolean banned){
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.deleteClass(banned, id, uri);
    }
}
