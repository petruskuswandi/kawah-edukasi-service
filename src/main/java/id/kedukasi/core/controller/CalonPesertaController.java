package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.PesertaRequest;
import id.kedukasi.core.service.CalonPesertaService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@CrossOrigin
@RestController
@RequestMapping("/calonPeserta")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CalonPesertaController {

    @Autowired
    CalonPesertaService service;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public Result getAllCalonPeserta() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllCalonPeserta(uri);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Result getCalonPesertaByid(@PathVariable("id") long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getCalonPesertaById(id, uri);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCalonPeserta(
            @Valid @RequestBody PesertaRequest pesertaRequest,
            @RequestParam(value = "kelasId", defaultValue = "0", required = true) long kelasId,
            @RequestPart("uploadImage") MultipartFile uploadImage) {
        return service.updateCalonPeserta(pesertaRequest, kelasId, uploadImage);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCalonPeserta(
            @Valid @RequestBody PesertaRequest pesertaRequest,
            @RequestParam(value = "kelasId", defaultValue = "0", required = true) long kelasId,
            @RequestPart("uploadImage") MultipartFile uploadImage) {
        return service.updateCalonPeserta(pesertaRequest, kelasId, uploadImage);
    }

    @PatchMapping(value = "/delete")
    public ResponseEntity<?> deleteCalonPeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id,
            @RequestParam(value = "banned", defaultValue = "true") boolean banned
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.deleteCalonPeserta(banned, id, uri);
    }

    @PatchMapping(value = "/changeToPeserta")
    public ResponseEntity<?> changeToPeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeToPeserta(id, uri);
    }

    @PatchMapping(value = "/changeStatusTes")
    public ResponseEntity<?> changeStatusTes(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id,
            @RequestParam(value = "Status Tes", defaultValue = "0") Long statusTesOrd
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeStatusTes(statusTesOrd, id, uri);
    }

    @PatchMapping(value = ("/changeKelas"))
    public ResponseEntity<?> changeKelas(
            @RequestParam(value = "calonPesertaId", defaultValue = "0", required = true) long calonPesertaId,
            @RequestParam(value = "kelasId", defaultValue = "0", required = true) long kelasId) {

        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeKelas(calonPesertaId, kelasId, uri);
    }


    @GetMapping("/filterByStatusTes")
    public Result filterByStatusTes(
            @RequestParam(value = "statusTesOrd", defaultValue = "0", required = true) Long statusTesOrd
    ) {
        return service.filterByStatusTes(statusTesOrd);
    }

    @GetMapping("/searchCalonPeserta")
    public Result searchCalonPeserta(@RequestParam(value = "keyword",required = true) String keyword) {
        return service.searchCalonPeserta(keyword);
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
