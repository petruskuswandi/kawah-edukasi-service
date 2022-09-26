package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.KelasRequest;
import id.kedukasi.core.service.KelasService;
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
@RequestMapping("/kelas")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class KelasController {

    @Autowired
    KelasService service;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public Result getAll() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllClass(uri);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Result getClassByid(@PathVariable("id") Long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getClassById(id, uri);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClass(@Valid @RequestBody KelasRequest kelasRequest) {
        return service.updateClass(kelasRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateClass(@Valid @RequestBody KelasRequest kelasRequest) {

        return service.updateClass(kelasRequest);
    }

    @PatchMapping(value = "/delete")
    public ResponseEntity<?> deleteClass(
            @RequestParam(value = "id", defaultValue = "0", required = true) Long id,
            @RequestParam(value = "banned", defaultValue = "true") boolean banned
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.deleteClass(banned, id, uri);
    }
}
