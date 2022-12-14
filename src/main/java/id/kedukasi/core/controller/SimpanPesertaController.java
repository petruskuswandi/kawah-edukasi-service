package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.service.CalonPesertaService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@CrossOrigin
@RestController
@RequestMapping("/simpanPeserta")
public class SimpanPesertaController {

    @Autowired
//    SimpanCalonPesertaService service;
    CalonPesertaService service;
    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public Result getAllCalonPeserta(
            @RequestParam(value = "search") String search,
            @RequestParam(value = "limit") long limit,
            @RequestParam(value = "offset") long offset
    ) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllCalonPeserta(uri,search,limit,offset);
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


//    @PostMapping("/create")
//    public ResponseEntity<?> createCalonPeserta(
//            @RequestParam(value = "Id_Program", defaultValue = "0") Long kelasId,
//            @RequestParam(value = "Id_Batch", defaultValue = "0") Long batchId,
//            @RequestParam(value = "Nama_Peserta") String namaPeserta,
//            @RequestParam(value = "Jenis_Kelamin") String jenisKelamin,
//            @RequestParam(value = "Tanggal_Lahir") String tanggalLahir,
//            @RequestParam(value = "Nomor_KTP") String nomorKtp,
//            @RequestParam(value = "Pendidikan_Terakhir") String pendidikanTerakhir,
//            @RequestParam(value = "No_Hp") String noHp,
//            @RequestParam(value = "Email") String email,
//            @RequestPart(value = "Upload_Image", required = false) MultipartFile uploadImage,
//            @RequestPart(value = "Upload_Cv", required = false) MultipartFile uploadCv,
//            @RequestParam(value = "Provinsi", defaultValue = "0") Long provinsi,
//            @RequestParam(value = "Kota", defaultValue = "0") Long kota,
//            @RequestParam(value = "Kecamatan", defaultValue = "0") Long kecamatan,
//            @RequestParam(value = "Kelurahan", defaultValue = "0") Long kelurahan,
//            @RequestParam(value = "Alamat_Rumah") String alamatRumah,
//            @RequestParam(value = "Motivasi") String motivasi,
//            @RequestParam(value = "Kode_Referal",required = false) String kodeReferal
//            ) {
//        Long id = 0L;
//        return service.updateCalonPeserta(id,kelasId,batchId, namaPeserta, nomorKtp, tanggalLahir,jenisKelamin, pendidikanTerakhir, noHp,
//                email, uploadImage, provinsi, kota, kecamatan, kelurahan, alamatRumah, motivasi, kodeReferal, uploadCv);
//    }

}
