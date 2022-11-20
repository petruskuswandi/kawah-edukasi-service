package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.service.PesertaService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


import java.util.Date;

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

    @GetMapping(value = "/allBanned", produces = APPLICATION_JSON_VALUE)
    public Result getAllBannedPeserta() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllBannedPeserta(uri);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Result getPesertaByid(@PathVariable("id") Long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getPesertaById(id, uri);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPeserta(
            @RequestParam(value = "Id Kelas", defaultValue = "0") Long kelasId,
            @RequestParam(value = "Id Batch", defaultValue = "0") Long batchId,
            @RequestParam(value = "Nama Peserta") String namaPeserta,
            @RequestParam(value = "Tanggal Lahir") String tanggalLahir,
            @RequestParam(value = "Jenis Kelamin") String jenisKelamin,
            @RequestParam(value = "Pendidikan Terakhir") String pendidikanTerakhir,
            @RequestParam(value = "No Hp") String noHp,
            @RequestParam(value = "Email") String email,
            @RequestPart(value = "Upload Image", required = false) MultipartFile uploadImage,
            @RequestParam(value = "Provinsi", defaultValue = "0") Long provinsi,
            @RequestParam(value = "Kota", defaultValue = "0") Long kota,
            @RequestParam(value = "Kecamatan", defaultValue = "0") Long kecamatan,
            @RequestParam(value = "Kelurahan", defaultValue = "0") Long kelurahan,
            @RequestParam(value = "Alamat Rumah") String alamatRumah,
            @RequestParam(value = "Motivasi") String motivasi,
            @RequestParam(value = "Kode Referal", required = false) String kodeReferal,
            @RequestParam(value = "Nomor Ktp",required = false) String nomorKtp
    ) {
        Long id = 0L;
        return service.updatePeserta(id,kelasId,batchId, namaPeserta,tanggalLahir,jenisKelamin, pendidikanTerakhir, noHp,
                email, uploadImage, provinsi, kota, kecamatan, kelurahan, alamatRumah, motivasi, kodeReferal, nomorKtp);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePeserta(
            @RequestParam(value = "Id") Long id,
            @RequestParam(value = "Id Batch") Long batchId,
            @RequestParam(value = "Id Kelas") Long kelasId,
            @RequestParam(value = "Nama Peserta") String namaPeserta,
            @RequestParam(value = "Tanggal Lahir") String tanggalLahir,
            @RequestParam(value = "Jenis Kelamin") String jenisKelamin,
            @RequestParam(value = "Pendidikan Terakhir") String pendidikanTerakhir,
            @RequestParam(value = "No Hp") String noHp,
            @RequestParam(value = "Email") String email,
            @RequestPart(value = "Upload Image", required = false) MultipartFile uploadImage,
            @RequestParam(value = "Provinsi") Long provinsi,
            @RequestParam(value = "Kota") Long kota,
            @RequestParam(value = "Kecamatan") Long kecamatan,
            @RequestParam(value = "Kelurahan") Long kelurahan,
            @RequestParam(value = "Alamat Rumah") String alamatRumah,
            @RequestParam(value = "Motivasi") String motivasi,
            @RequestParam(value = "Kode Referal", required = false) String kodeReferal,
            @RequestParam(value = "Nomor Ktp",required = false) String nomorKtp
    ) {
        return service.updatePeserta(id,kelasId,batchId, namaPeserta,tanggalLahir,jenisKelamin, pendidikanTerakhir, noHp,
                email, uploadImage, provinsi, kota, kecamatan, kelurahan, alamatRumah, motivasi, kodeReferal, nomorKtp);
    }

    @PatchMapping(value = "/delete")
    public ResponseEntity<?> deletePeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) Long id,
            @RequestParam(value = "banned", defaultValue = "true") boolean banned
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.deletePeserta(banned, id, uri);
    }

    @PatchMapping(value = "/changeToCalonPeserta")
    public ResponseEntity<?> changeToCalonPeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) Long id
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeToCalonPeserta(id, uri);
    }

    @PatchMapping(value = ("/changeKelas"))
    public ResponseEntity<?> changeKelas(
            @RequestParam(value = "pesertaId", defaultValue = "0", required = true) Long pesertaId,
            @RequestParam(value = "kelasId", defaultValue = "0", required = true) Long kelasId) {

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
            @RequestParam(value = "ascending", defaultValue = "true") Boolean ascending
    ) {
        return service.sortAndPaging(page, size, ascending);
    }
}
