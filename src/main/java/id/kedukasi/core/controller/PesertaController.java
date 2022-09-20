package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.PesertaRequest;
import id.kedukasi.core.service.PesertaService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/peserta")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PesertaController {

    @Autowired
    PesertaService service;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public Result getAllPeserta() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllPeserta(uri);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Result getPesertaByid(@PathVariable("id") long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getPesertaById(id, uri);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPeserta(
            @Valid @RequestBody PesertaRequest pesertaRequest,
            @RequestParam(value = "kelasId", defaultValue = "0", required = true) long kelasId,
            @RequestPart("uploadImage") MultipartFile uploadImage) {
        return service.updatePeserta(pesertaRequest, kelasId, uploadImage);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePeserta(
            @Valid @RequestBody PesertaRequest pesertaRequest,
            @RequestParam(value = "kelasId", defaultValue = "0", required = true) long kelasId,
            @RequestPart("uploadImage") MultipartFile uploadImage) {
        return service.updatePeserta(pesertaRequest, kelasId, uploadImage);
    }

    @PatchMapping(value = "/delete")
    public ResponseEntity<?> deletePeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id,
            @RequestParam(value = "banned", defaultValue = "true") boolean banned
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.deletePeserta(banned, id, uri);
    }

    @PatchMapping(value = "/changeToCalonPeserta")
    public ResponseEntity<?> changeToCalonPeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeToCalonPeserta(id, uri);
    }

    @PatchMapping(value = ("/changeKelas"))
    public ResponseEntity<?> changeKelas(
            @RequestParam(value = "pesertaId", defaultValue = "0", required = true) long pesertaId,
            @RequestParam(value = "kelasId", defaultValue = "0", required = true) long kelasId) {

        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeKelas(pesertaId, kelasId, uri);
    }

    @GetMapping("/searchPeserta")
    public Result searchPeserta(@RequestParam(value = "keyword",required = true) String keyword) {
        return service.searchPeserta(keyword);
    }

    @GetMapping("/sortAndPaging")
    public Result sortAndPaging(
            @RequestParam(value = "page", defaultValue = "0", required = true) Integer page,
            @RequestParam(value = "size", defaultValue = "1", required = true) Integer size,
            @RequestParam(value = "ascending", defaultValue = "true", required = true) Boolean ascending
    ) {
        return service.sortAndPaging(page, size, ascending);
    }
}
