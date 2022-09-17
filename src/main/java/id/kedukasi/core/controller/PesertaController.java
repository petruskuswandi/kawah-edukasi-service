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

import java.util.List;

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
    public Result getAll() {
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
    public ResponseEntity<?> createPeserta(@Valid @RequestBody PesertaRequest pesertaRequest) {
        return service.updatePeserta(pesertaRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePeserta(@Valid @RequestBody PesertaRequest pesertaRequest) {
        return service.updatePeserta(pesertaRequest);
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

    @PatchMapping(value = "/statusPeserta")
    public ResponseEntity<?> statusPeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id,
            @RequestParam(value = "Status Peserta", defaultValue = "0") Long statusPesertaOrd
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.statusPeserta(statusPesertaOrd, id, uri);
    }

    @PatchMapping(value = "/statusTes")
    public ResponseEntity<?> statusTes(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id,
            @RequestParam(value = "Status Tes", defaultValue = "0") Long statusTesOrd
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.statusTes(statusTesOrd, id, uri);
    }

    @PatchMapping(value = "/updateUploadImageBlob", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfilePicture(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id,
            @RequestPart("uploadImage") MultipartFile profilePicture) {

        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.updateUploadImageBlob(id, profilePicture, uri);
    }

    @PatchMapping("/updateUploadImageFolder")
    public ResponseEntity<?> updateProfilePictureFolder(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id,
            @RequestPart("uploadImage") MultipartFile profilePicture) {

        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.updateUploadImageFolder(id, profilePicture, uri);
    }

    @GetMapping("/filterByStatusPeserta")
    public Result filterByStatusPeserta(
            @RequestParam(value = "filterByStatusPeserta", defaultValue = "0", required = true) Long statusPesertaOrd
    ) {
        return service.filterByStatusPeserta(statusPesertaOrd);
    }


//    "/getProvinsi"
//    "/getProvinsi/{id}"
//    "/getKota"
//    "/getKota/{id}"
//    "/getKecamatan"
//    "/getKecamatan/{id}"
//    "/getKelurahan"
//    "/getkelurahan/{id}"
//    "activeInactive"
//    "search"
//    "filterByBatch"
//    "filterByStatus"
//    "indexAndPaging"
}
