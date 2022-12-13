package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.service.PesertaService;
import id.kedukasi.core.service.UpdatePesertaService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@CrossOrigin
@RestController
@RequestMapping("/peserta/v2/")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UpdatePesertaController {
    @Autowired
    private ServletContext servletContext;

    @Autowired
    UpdatePesertaService service;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getPesertaByid(@PathVariable("id") Long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getPesertaById(id, uri);
    }

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getAllPeserta(@RequestParam(required = false,name = "search") String search,
                                                @RequestParam(required = false,name = "status id") int status_id,
                                                @RequestParam(value = "limit",defaultValue = "10") long limit,
                                                @RequestParam(value = "offset",defaultValue = "0") long offset) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllPeserta(status_id,uri,search,limit,offset);
        // return service.getAllPeserta(uri,search,limit,offset);
    }


    @PatchMapping(value = "/changeStatus")
    public ResponseEntity<Result> changeStatus(
            @RequestParam(value = "id", defaultValue = "0", required = true) Long id,
            @RequestParam(value = "status id", defaultValue = "0", required = true) int statusId
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeStatusPeserta(statusId, id, uri);
        // return service.changeToCalonPeserta(id, uri);
    }



}
