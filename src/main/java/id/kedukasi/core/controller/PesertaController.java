package id.kedukasi.core.controller;

import com.google.gson.Gson;
import com.lowagie.text.DocumentException;

import id.kedukasi.core.enums.EnumStatusTes;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.RegisterRequest;
import id.kedukasi.core.service.PesertaService;
import id.kedukasi.core.utils.StringUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@RestController
@RequestMapping("/peserta")
// @PreAuthorize("hasRole('ROLE_ADMIN')")
public class PesertaController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    PesertaService service;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public Result getAllPeserta(@RequestParam(required = false, name = "search") String search,
            @RequestParam(value = "limit", defaultValue = "-99") long limit,
            @RequestParam(value = "offset", defaultValue = "-99") long offset) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllPeserta(uri, search, limit, offset);
    }

    @GetMapping(value = "/allBanned", produces = APPLICATION_JSON_VALUE)
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result getAllBannedPeserta() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllBannedPeserta(uri);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result getPesertaByid(@PathVariable("id") Long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getPesertaById(id, uri);
    }

    @GetMapping(value = "/image-response-entity/{id}", produces = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE
    })
    public @ResponseBody byte[] getImageAsResponseEntity(@PathVariable("id") String id) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("templates/" + id);
        byte[] media = IOUtils.toByteArray(in);
        return media;
    }

    @RequestMapping(path = "/register", method = POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Result> saveRegister(@RequestPart String register, @RequestPart List<MultipartFile> files)
            throws MessagingException, ParseException, IOException, DocumentException {
        return service.registerPeserta(null, register, files);
    }

    @PostMapping("/create")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createPeserta(
            @RequestParam(value = "Id Kelas", defaultValue = "0") Long kelasId,
            @RequestParam(value = "Id Batch", defaultValue = "0") Long batchId,
            @RequestParam(value = "Nama Peserta") String namaPeserta,
            @RequestParam(value = "Tanggal Lahir") String tanggalLahir,
            @RequestParam(value = "Jenis Kelamin") String jenisKelamin,
            @RequestParam(value = "Id Education") String pendidikanTerakhir,
            @RequestParam(value = "No Hp") String noHp,
            @RequestParam(value = "Email") String email,
            @RequestPart(value = "Upload Image", required = false) MultipartFile uploadImage,
            @RequestParam(value = "Id Provinsi", defaultValue = "0") Long provinsi,
            @RequestParam(value = "Id Kota", defaultValue = "0") Long kota,
            @RequestParam(value = "Id Kecamatan", defaultValue = "0") Long kecamatan,
            @RequestParam(value = "Id Kelurahan", defaultValue = "0") Long kelurahan,
            @RequestParam(value = "Alamat Rumah") String alamatRumah,
            @RequestParam(value = "Motivasi") String motivasi,
            @RequestParam(value = "Kode Referal", required = false) String kodeReferal,
            @RequestParam(value = "Nomor Ktp", required = false) String nomorKtp,

            // tambahan
            @RequestPart(value = "Upload CV", required = false) MultipartFile uploadCv,
            @RequestParam(value = "kesibukan", required = false) Integer kesibukan,
            @RequestParam(value = "score Tets Awal", required = false) Integer scoreTetsAwal,
            @RequestParam(value = "score Test Akhir", required = false) Integer scoreTestAkhir,
            @RequestParam(value = "status Id", required = false) Integer status,
            @RequestParam(value = "nama project") String namaProject,
            @RequestParam(value = "jurusan") String jurusan) {
        Long id = 0L;
        return service.updatePeserta(id, kelasId, batchId, namaPeserta, tanggalLahir, jenisKelamin, pendidikanTerakhir,noHp,email, uploadImage, provinsi, kota, kecamatan, kelurahan, alamatRumah, motivasi,kodeReferal, nomorKtp,uploadCv, kesibukan, scoreTetsAwal, scoreTestAkhir, status, namaProject, jurusan);
    }

    @PutMapping("/update")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
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
            @RequestParam(value = "Nomor Ktp", required = false) String nomorKtp,

            // tambahan
            @RequestPart(value = "Upload CV", required = false) MultipartFile uploadCv,
            @RequestParam(value = "kesibukan", required = false) Integer kesibukan,
            @RequestParam(value = "score Tets Awal", required = false) Integer scoreTetsAwal,
            @RequestParam(value = "score Test Akhir", required = false) Integer scoreTestAkhir,
            @RequestParam(value = "status Id", required = false) Integer status,
            @RequestParam(value = "nama project") String namaProject,
            @RequestParam(value = "jurusan") String jurusan) {

        return service.updatePeserta(id, kelasId, batchId, namaPeserta, tanggalLahir, jenisKelamin, pendidikanTerakhir,noHp,email, uploadImage, provinsi, kota, kecamatan, kelurahan, alamatRumah, motivasi,kodeReferal, nomorKtp,uploadCv, kesibukan, scoreTetsAwal, scoreTestAkhir, status, namaProject, jurusan);
    }

    @PatchMapping(value = "/delete")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deletePeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) Long id,
            @RequestParam(value = "banned", defaultValue = "true") boolean banned) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.deletePeserta(banned, id, uri);
    }

    @PatchMapping(value = "/changeToCalonPeserta")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeToCalonPeserta(
            @RequestParam(value = "id", defaultValue = "0", required = true) Long id) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeToCalonPeserta(id, uri);
    }

    @PatchMapping(value = ("/changeKelas"))
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeKelas(
            @RequestParam(value = "pesertaId", defaultValue = "0", required = true) Long pesertaId,
            @RequestParam(value = "kelasId", defaultValue = "0", required = true) Long kelasId) {

        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.changeKelas(pesertaId, kelasId, uri);
    }

    @GetMapping("/searchPeserta")
    public Result searchPeserta(@RequestParam(value = "keyword", required = true) String keyword) {
        return service.searchPeserta(keyword);
    }

    @GetMapping("/sortAndPaging")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result sortAndPaging(
            @RequestParam(value = "page", defaultValue = "0", required = true) Integer page,
            @RequestParam(value = "size", defaultValue = "1", required = true) Integer size,
            @RequestParam(value = "ascending", defaultValue = "true") Boolean ascending) {
        return service.sortAndPaging(page, size, ascending);
    }
}
