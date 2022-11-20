package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.enums.EnumStatusTes;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.BatchRepository;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.PesertaRepository;
import id.kedukasi.core.repository.wilayah.KecamatanRepository;
import id.kedukasi.core.repository.wilayah.KelurahanRepository;
import id.kedukasi.core.repository.wilayah.KotaRepository;
import id.kedukasi.core.repository.wilayah.ProvinsiRepository;
import id.kedukasi.core.service.CalonPesertaService;
import id.kedukasi.core.utils.StringUtil;
import id.kedukasi.core.utils.ValidatorUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CalonPesertaServiceImpl implements CalonPesertaService {

    @Autowired
    PesertaRepository pesertaRepository;

    @Autowired
    KelasRepository kelasRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    ProvinsiRepository provinsiRepository;

    @Autowired
    KotaRepository kotaRepository;

    @Autowired
    KecamatanRepository kecamatanRepository;

    @Autowired
    KelurahanRepository kelurahanRepository;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    ValidatorUtil validator;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Result getAllCalonPeserta(String uri, String search) {
        result = new Result();
        try {
            Map items = new HashMap();
            List<Peserta> getDataCalon = pesertaRepository.getCalonPeserta(search);
            List<Peserta> getTotal = pesertaRepository.getCalonPeserta("");
            logger.info("cek "+getDataCalon.get(0).getTanggalLahir());
            items.put("items", getDataCalon);
            items.put("totalItems", getDataCalon.size());
            items.put("totalData", getTotal);
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getAllBannedCalonPeserta(String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            Peserta calonPeserta = new Peserta();
            calonPeserta.setStatusPeserta(EnumStatusPeserta.CALON);
            calonPeserta.setBanned(false);
            Example<Peserta> example = Example.of(calonPeserta);
            items.put("items", pesertaRepository.findAll(example,Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getCalonPesertaById(Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada peserta dengan id: " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("Error: id "+ id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", pesertaRepository.findById(id).get());
                result.setData(items);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public ResponseEntity<?> updateCalonPeserta(Long id, Long kelasId,Long batchId, String namaPeserta, String tanggalLahir,
                                                String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
                                                MultipartFile uploadImage, Long provinsiId, Long kotaId, Long kecamatanId,
                                                Long kelurahanId, String alamatRumah, String motivasi, String kodeReferal, String nomorKtp, MultipartFile uploadCv) {
        result = new Result();
        try {
            //cek email
            Peserta checkEmailPeserta = pesertaRepository.findByEmail(email).orElse(new Peserta());
            if (checkEmailPeserta.getEmail() != null && !Objects.equals(id, checkEmailPeserta.getId())) {
                result.setMessage("Error: Email sudah digunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            if (email.isBlank()) {
                result.setMessage("Error: Email tidak boleh kosong!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
//            if (nomorKtp.isBlank()) {
//                result.setMessage("Error: KTP tidak boleh kosong!");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
            //cek username
            Peserta checkNamaPeserta = pesertaRepository.findByNamaPeserta(namaPeserta).orElse(new Peserta());
            if (checkNamaPeserta.getNamaPeserta() != null && !Objects.equals(id, checkNamaPeserta.getId())) {
                result.setMessage("Error: Username sudah digunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            if (namaPeserta.isBlank()) {
                result.setMessage("Error: Nama Peserta tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            //cek tanggal lahir
            if (tanggalLahir.isBlank()) {
                result.setMessage("Error: Tanggal lahir tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            Date tanggalLahirTypeDate = new SimpleDateFormat("dd-MM-yyyy").parse(tanggalLahir);
            //cek jenis kelamin
            if (jenisKelamin.isBlank()) {
                result.setMessage("Error: Jenis kelamin tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            //cek pendidikan terakhir
            if (pendidikanTerakhir.isBlank()) {
                result.setMessage("Error: Pendidikan terakhir tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            //cek alamat rumah
            if (alamatRumah.isBlank()) {
                result.setMessage("Error: Alamat rumah tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            //cek motivasi
            if (motivasi.isBlank()) {
                result.setMessage("Error: Motivasi tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            //cek nomer hp
            if (!validator.isPhoneValid(noHp)) {
                result.setMessage("Error: nomor telepon tidak boleh kosong dan gunakan format 08xxx/+628xxx/628xxx!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            //cek status peserta
            Peserta checkStatusPeserta = pesertaRepository.findById(id).orElse(new Peserta());
            if (checkStatusPeserta.getStatusPeserta() != null && !Objects.equals(EnumStatusPeserta.CALON, checkStatusPeserta.getStatusPeserta())) {
                result.setMessage("Error: id: " + id + " bukan Calon Peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            Peserta peserta = null ;
            Long newId = null;
            if(id== Long.valueOf(0)){
                peserta = new Peserta(namaPeserta, tanggalLahirTypeDate, jenisKelamin, pendidikanTerakhir, noHp, email,
                        alamatRumah, motivasi, kodeReferal, nomorKtp);
                newId = Long.valueOf(pesertaRepository.getPesertaMaxId());
                peserta.setId(newId);
            }else{
                peserta = pesertaRepository.getPesertaById(id);
                peserta.setNamaPeserta(namaPeserta);
                peserta.setTanggalLahir(tanggalLahirTypeDate);
                peserta.setJenisKelamin(jenisKelamin);
                peserta.setPendidikanTerakhir(pendidikanTerakhir);
                peserta.setNoHp(noHp);
                peserta.setEmail(email);
                peserta.setAlamatRumah(alamatRumah);
                peserta.setMotivasi(motivasi);
                peserta.setKodeReferal(kodeReferal);
                peserta.setNomorKtp(nomorKtp);
            }

            peserta.setStatusPeserta(EnumStatusPeserta.CALON);

            //set kelas
            if (!kelasRepository.findById(kelasId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kelas dengan id " + kelasId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setKelas(kelasRepository.findById(kelasId).get());
            }

            //set batch
            if (!batchRepository.findById(batchId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada batch dengan id " + batchId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setBatch(batchRepository.findById(batchId).get());
            }

            //set image
            if (uploadImage != null) {
                peserta.setUploadImage(IOUtils.toByteArray(uploadImage.getInputStream()));
            }
            if (uploadCv != null) {
                peserta.setUploadCv(IOUtils.toByteArray(uploadImage.getInputStream()));
            }

            //set provinsi
            if (!provinsiRepository.findById(provinsiId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada provinsi dengan id " + provinsiId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setProvinsi(provinsiRepository.findById(provinsiId).get());
            }

            //set kota
            if (!kotaRepository.findById(kotaId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kota dengan id " + kotaId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setKota(kotaRepository.findById(kotaId).get());
            }

            //set kecamatan
            if (!kecamatanRepository.findById(kecamatanId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kecamatan dengan id " + kecamatanId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setKecamatan(kecamatanRepository.findById(kecamatanId).get());
            }

            //set kelurahan
            if (!kelurahanRepository.findById(kelurahanId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kelurahan dengan id " + kelurahanId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setKelurahan(kelurahanRepository.findById(kelurahanId).get());
            }
            logger.info("tes "+peserta.getId());
            pesertaRepository.save(peserta);

            result.setMessage(id == 0 ? "Berhasil membuat calon peserta baru!" : "Berhasil memperbarui calon peserta!");
            result.setCode(HttpStatus.OK.value());
        } catch (ParseException e) {
            result.setSuccess(false);
            result.setMessage("Error: Gunakan format \"dd-MM-yyyy\" pada tanggal lahir");
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
                    .badRequest()
                    .body(result);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

//    @Override
//    public ResponseEntity<?> updateCalonPeserta(Long id, Long kelasId,Long batchId, String namaPeserta, String tanggalLahir,
//                                                String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
//                                                MultipartFile uploadImage, Long provinsiId, Long kotaId, Long kecamatanId,
//                                                Long kelurahanId, String alamatRumah, String motivasi, String kodeReferal, String nomorKtp, MultipartFile uploadCv) {
//        result = new Result();
//        try {
//            //cek email
//            Peserta checkEmailPeserta = pesertaRepository.findByEmail(email).orElse(new Peserta());
//            if (checkEmailPeserta.getEmail() != null && !Objects.equals(id, checkEmailPeserta.getId())) {
//                result.setMessage("Error: Email sudah digunakan!");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//
//            if (email.isBlank()) {
//                result.setMessage("Error: Email tidak boleh kosong!");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//
////            if (nomorKtp.isBlank()) {
////                result.setMessage("Error: Nomor KTP tidak boleh kosong!");
////                result.setCode(HttpStatus.BAD_REQUEST.value());
////                return ResponseEntity
////                        .badRequest()
////                        .body(result);
////            }
//            //cek username
//            Peserta checkNamaPeserta = pesertaRepository.findByNamaPeserta(namaPeserta).orElse(new Peserta());
//            if (checkNamaPeserta.getNamaPeserta() != null && !Objects.equals(id, checkNamaPeserta.getId())) {
//                result.setMessage("Error: Username sudah digunakan!");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//            if (namaPeserta.isBlank()) {
//                result.setMessage("Error: Nama Peserta tidak boleh kosong");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//            //cek tanggal lahir
//            if (tanggalLahir.isBlank()) {
//                result.setMessage("Error: Tanggal lahir tidak boleh kosong");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//            Date tanggalLahirTypeDate = new SimpleDateFormat("dd-MM-yyyy").parse(tanggalLahir);
//            //cek jenis kelamin
//            if (jenisKelamin.isBlank()) {
//                result.setMessage("Error: Jenis kelamin tidak boleh kosong");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//            //cek pendidikan terakhir
//            if (pendidikanTerakhir.isBlank()) {
//                result.setMessage("Error: Pendidikan terakhir tidak boleh kosong");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//            //cek alamat rumah
//            if (alamatRumah.isBlank()) {
//                result.setMessage("Error: Alamat rumah tidak boleh kosong");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//            //cek motivasi
//            if (motivasi.isBlank()) {
//                result.setMessage("Error: Motivasi tidak boleh kosong");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//            //cek nomer hp
//            if (!validator.isPhoneValid(noHp)) {
//                result.setMessage("Error: nomor telepon tidak boleh kosong dan gunakan format 08xxx/+628xxx/628xxx!");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//            //cek status peserta
//            Peserta checkStatusPeserta = pesertaRepository.findById(id).orElse(new Peserta());
//            if (checkStatusPeserta.getStatusPeserta() != null && !Objects.equals(EnumStatusPeserta.CALON, checkStatusPeserta.getStatusPeserta())) {
//                result.setMessage("Error: id: " + id + " bukan Calon Peserta");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }
//
//            Peserta peserta = new Peserta(namaPeserta, tanggalLahirTypeDate, jenisKelamin, pendidikanTerakhir, noHp, email,
//                    alamatRumah, motivasi, kodeReferal, nomorKtp);
//
//            peserta.setId(id);
//            peserta.setStatusPeserta(EnumStatusPeserta.CALON);
//
//            //set kelas
//            if (!kelasRepository.findById(kelasId).isPresent()) {
//                result.setSuccess(false);
//                result.setMessage("Error: Tidak ada kelas dengan id " + kelasId);
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            } else {
//                peserta.setKelas(kelasRepository.findById(kelasId).get());
//            }
//
//            //set batch
//            if (!batchRepository.findById(batchId).isPresent()) {
//                result.setSuccess(false);
//                result.setMessage("Error: Tidak ada batch dengan id " + batchId);
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            } else {
//                peserta.setBatch(batchRepository.findById(batchId).get());
//            }
//
//            //set image
//            if (uploadImage != null) {
//                peserta.setUploadImage(IOUtils.toByteArray(uploadImage.getInputStream()));
//            }
//
//            //set provinsi
//            if (!provinsiRepository.findById(provinsiId).isPresent()) {
//                result.setSuccess(false);
//                result.setMessage("Error: Tidak ada provinsi dengan id " + provinsiId);
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            } else {
//                peserta.setProvinsi(provinsiRepository.findById(provinsiId).get());
//            }
//
//            //set kota
//            if (!kotaRepository.findById(kotaId).isPresent()) {
//                result.setSuccess(false);
//                result.setMessage("Error: Tidak ada kota dengan id " + kotaId);
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            } else {
//                peserta.setKota(kotaRepository.findById(kotaId).get());
//            }
//
//            //set kecamatan
//            if (!kecamatanRepository.findById(kecamatanId).isPresent()) {
//                result.setSuccess(false);
//                result.setMessage("Error: Tidak ada kecamatan dengan id " + kecamatanId);
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            } else {
//                peserta.setKecamatan(kecamatanRepository.findById(kecamatanId).get());
//            }
//
//            //set kelurahan
//            if (!kelurahanRepository.findById(kelurahanId).isPresent()) {
//                result.setSuccess(false);
//                result.setMessage("Error: Tidak ada kelurahan dengan id " + kelurahanId);
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            } else {
//                peserta.setKelurahan(kelurahanRepository.findById(kelurahanId).get());
//            }
//
//            pesertaRepository.save(peserta);
//
//            result.setMessage(id == 0 ? "Berhasil membuat calon peserta baru!" : "Berhasil memperbarui calon peserta!");
//            result.setCode(HttpStatus.OK.value());
//        } catch (ParseException e) {
//            result.setSuccess(false);
//            result.setMessage("Error: Gunakan format \"dd-MM-yyyy\" pada tanggal lahir");
//            result.setCode(HttpStatus.BAD_REQUEST.value());
//            return ResponseEntity
//                    .badRequest()
//                    .body(result);
//        } catch (Exception e) {
//            logger.error(stringUtil.getError(e));
//        }
//        return ResponseEntity.ok(result);
//    }

    @Override
    public ResponseEntity<?> deleteCalonPeserta(boolean banned, Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("Error: id "+ id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                pesertaRepository.deletePeserta(banned, id);
                if (banned) {
                    result.setMessage("Berhasil menghapus calon peserta");
                } else {
                    result.setMessage("Berhasil mengembalikan calon peserta");
                }
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> changeToPeserta(Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("Error: id "+ id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                pesertaRepository.statusPeserta(EnumStatusPeserta.PESERTA, id);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> changeStatusTes(Long statusTesOrd, Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("Error: id "+ id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                if (statusTesOrd==0) {
                    pesertaRepository.statusTes(EnumStatusTes.LULUS, id);
                } else if (statusTesOrd==1) {
                    pesertaRepository.statusTes(EnumStatusTes.MELAKSANAKANTES, id);
                } else if (statusTesOrd==2) {
                    pesertaRepository.statusTes(EnumStatusTes.MENUNGGUFOLLOWUP, id);
                } else {
                    result.setMessage("Error: gunakan 0 untuk Lulus, 1 untuk Melaksanakan Tes dan 2 untuk Menunggu Follow Up");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                    return ResponseEntity
                            .badRequest()
                            .body(result);
                }
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> changeKelas(Long calonPesertaId, Long kelasId, String uri) {
        result = new Result();
        try {
            Peserta calonPeserta = pesertaRepository.findById(calonPesertaId).get();
            Kelas kelas = kelasRepository.findById(kelasId).get();
            if (!pesertaRepository.findById(calonPesertaId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id " + calonPesertaId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (!kelasRepository.findById(kelasId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kelas dengan id " + kelasId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (calonPeserta.getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("Error: id "+ calonPesertaId + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                calonPeserta.setKelas(kelas);
                pesertaRepository.save(calonPeserta);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public Result filterByStatusTes(Long statusTesOrd) {
        result = new Result();
        try {
            Map items = new HashMap();
            Peserta peserta = new Peserta();
            if (statusTesOrd==0) {
                peserta.setStatusTes(EnumStatusTes.MELAKSANAKANTES);
                peserta.setStatusPeserta(EnumStatusPeserta.CALON);
                Example<Peserta> example = Example.of(peserta);
                items.put("items", pesertaRepository.findAll(example,Sort.by(Sort.Direction.ASC,"id")));
                result.setData(items);
            } else if (statusTesOrd==1) {
                peserta.setStatusTes(EnumStatusTes.MENUNGGUFOLLOWUP);
                peserta.setStatusPeserta(EnumStatusPeserta.CALON);
                Example<Peserta> example = Example.of(peserta);
                items.put("items", pesertaRepository.findAll(example,Sort.by(Sort.Direction.ASC,"id")));
                result.setData(items);
            } else if (statusTesOrd==2) {
                peserta.setStatusTes(EnumStatusTes.LULUS);
                peserta.setStatusPeserta(EnumStatusPeserta.CALON);
                Example<Peserta> example = Example.of(peserta);
                items.put("items", pesertaRepository.findAll(example,Sort.by(Sort.Direction.ASC,"id")));
                result.setData(items);
            } else {
                result.setMessage("Error: gunakan 0 untuk Melaksanakan Tes, 1 untuk Menunggu Follow Up dan 2 untuk Lulus");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return result;
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result searchCalonPeserta(String keyword) {
        result = new Result();
        try {
            Map items = new HashMap();
            items.put("items", pesertaRepository.search(keyword,EnumStatusPeserta.CALON));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result sortAndPaging(Integer page, Integer size, Boolean ascending) {
        result = new Result();
        try {
            Map items = new HashMap();
            Peserta calonPeserta = new Peserta();
            calonPeserta.setStatusPeserta(EnumStatusPeserta.CALON);
            Example<Peserta> example = Example.of(calonPeserta);
            if (ascending) {
                Page<Peserta> pagePeserta = pesertaRepository.findAll(example,
                        PageRequest.of(page, size, Sort.by("id").ascending()));
                items.put("items", pagePeserta);
            }
            else {
                Page<Peserta> pagePeserta = pesertaRepository.findAll(example,
                        PageRequest.of(page, size, Sort.by("id").descending()));
                items.put("items", pagePeserta);
            }
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }
}
