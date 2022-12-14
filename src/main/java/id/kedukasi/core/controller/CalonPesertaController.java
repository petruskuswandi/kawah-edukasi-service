package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
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
    public Result getAllCalonPeserta(
            @RequestParam(value = "search") String search
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info("tes "+uri);
        return service.getAllCalonPeserta(uri,search);
    }

    @GetMapping(value = "/allBanned", produces = APPLICATION_JSON_VALUE)
    public Result getAllBannedCalonPeserta() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllBannedCalonPeserta(uri);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Result getCalonPesertaByid(@PathVariable("id") Long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getCalonPesertaById(id, uri);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCalonPeserta(
            @RequestParam(value = "Id Kelas", defaultValue = "0") Long kelasId,
            @RequestParam(value = "Id Batch", defaultValue = "0") Long batchId,
            @RequestParam(value = "Nomor Ktp", defaultValue = "0") String nomorKtp,
            @RequestParam(value = "Nama Peserta") String namaPeserta,
            @RequestParam(value = "Tanggal Lahir") String tanggalLahir,
            @RequestParam(value = "Jenis Kelamin") String jenisKelamin,
            @RequestParam(value = "Pendidikan Terakhir") String pendidikanTerakhir,
            @RequestParam(value = "No Hp") String noHp,
            @RequestParam(value = "Email") String email,
            @RequestPart(value = "Upload Image", required = false) MultipartFile uploadImage,
            @RequestPart(value = "Upload Cv", required = false) MultipartFile uploadCv,
            @RequestParam(value = "Provinsi", defaultValue = "0") Long provinsi,
            @RequestParam(value = "Kota", defaultValue = "0") Long kota,
            @RequestParam(value = "Kecamatan", defaultValue = "0") Long kecamatan,
            @RequestParam(value = "Kelurahan", defaultValue = "0") Long kelurahan,
            @RequestParam(value = "Alamat Rumah") String alamatRumah,
            @RequestParam(value = "Motivasi") String motivasi,
            @RequestParam(value = "Kode Referal",required = false) String kodeReferal
    ) {
        Long id = 0L;
        return service.updateCalonPeserta(id,kelasId,batchId, namaPeserta,tanggalLahir,jenisKelamin, pendidikanTerakhir, noHp,
                email, uploadImage, provinsi, kota, kecamatan, kelurahan, alamatRumah, motivasi, kodeReferal, nomorKtp, uploadCv);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCalonPeserta(
            @RequestParam(value = "Id") Long id,
            @RequestParam(value = "Id Kelas", defaultValue = "0") Long kelasId,
            @RequestParam(value = "Id Batch", defaultValue = "0") Long batchId,
            @RequestParam(value = "Nomor Ktp", defaultValue = "0") String nomorKtp,
            @RequestParam(value = "Nama Peserta") String namaPeserta,
            @RequestParam(value = "Tanggal Lahir") String tanggalLahir,
            @RequestParam(value = "Jenis Kelamin") String jenisKelamin,
            @RequestParam(value = "Pendidikan Terakhir") String pendidikanTerakhir,
            @RequestParam(value = "No Hp") String noHp,
            @RequestParam(value = "Email") String email,
            @RequestPart(value = "Upload Image", required = false) MultipartFile uploadImage,
            @RequestPart(value = "Upload Cv", required = false) MultipartFile uploadCv,
            @RequestParam(value = "Provinsi", defaultValue = "0") Long provinsi,
            @RequestParam(value = "Kota", defaultValue = "0") Long kota,
            @RequestParam(value = "Kecamatan", defaultValue = "0") Long kecamatan,
            @RequestParam(value = "Kelurahan", defaultValue = "0") Long kelurahan,
            @RequestParam(value = "Alamat Rumah") String alamatRumah,
            @RequestParam(value = "Motivasi") String motivasi,
            @RequestParam(value = "Kode Referal",required = false) String kodeReferal
    ) {
        return service.updateCalonPeserta(id,kelasId,batchId, namaPeserta,tanggalLahir,jenisKelamin, pendidikanTerakhir, noHp,
                email, uploadImage, provinsi, kota, kecamatan, kelurahan, alamatRumah, motivasi, kodeReferal, nomorKtp, uploadCv);
    }

    @PatchMapping(value = "/delete")
    public ResponseEntity<?> deleteCalonPeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) Long id,
            @RequestParam(value = "banned", defaultValue = "true") boolean banned
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.deleteCalonPeserta(banned, id, uri);
    }

    @PatchMapping(value = "/changeToPeserta")
    public ResponseEntity<?> changeToPeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) Long id
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeToPeserta(id, uri);
    }

    @PatchMapping(value = "/changeStatusTes")
    public ResponseEntity<?> changeStatusTes(
            @RequestParam(value = "id", defaultValue = "0", required = true) Long id,
            @RequestParam(value = "Status Tes", defaultValue = "0") Long statusTesOrd
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeStatusTes(statusTesOrd, id, uri);
    }

    @PatchMapping(value = ("/changeKelas"))
    public ResponseEntity<?> changeKelas(
            @RequestParam(value = "calonPesertaId", defaultValue = "0", required = true) Long calonPesertaId,
            @RequestParam(value = "kelasId", defaultValue = "0", required = true) Long kelasId) {

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

    @PatchMapping("/uploadImagePath")
    public ResponseEntity<?> updateProfilePictureFolder(
            @RequestParam(value = "id", defaultValue = "0", required = true) long id,
            @RequestPart("uploadImagePath") MultipartFile uploadImagePath) {

        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.setUploadImagePath(id, uploadImagePath, uri);
    }
}
