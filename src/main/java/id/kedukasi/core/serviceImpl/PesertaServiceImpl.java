package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.wilayah.MasterKecamatan;
import id.kedukasi.core.models.wilayah.MasterKelurahan;
import id.kedukasi.core.models.wilayah.MasterKota;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
import id.kedukasi.core.repository.BatchRepository;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.PesertaRepository;
import id.kedukasi.core.repository.wilayah.KecamatanRepository;
import id.kedukasi.core.repository.wilayah.KelurahanRepository;
import id.kedukasi.core.repository.wilayah.KotaRepository;
import id.kedukasi.core.repository.wilayah.ProvinsiRepository;
import id.kedukasi.core.service.PesertaService;
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


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PesertaServiceImpl implements PesertaService {

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
    public Result getAllPeserta(String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            Peserta peserta = new Peserta();
            peserta.setStatusPeserta(EnumStatusPeserta.PESERTA);
            Example<Peserta> example = Example.of(peserta);
            items.put("items", pesertaRepository.findAll(example));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getPesertaById(Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("cannot find peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
                result.setSuccess(false);
                result.setMessage("id: "+ id + " is not peserta");
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
    public ResponseEntity<?> updatePeserta(Long id, Long kelasId,Long batchId, String namaPeserta, Date tanggalLahir,
                                           String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
                                           MultipartFile uploadImage, Long provinsiId, Long kotaId, Long kecamatanId,
                                           Long kelurahanId, String alamatRumah, String motivasi, String kodeReferal) {
        result = new Result();
        try {
            Peserta checkEmailPeserta = pesertaRepository.findByEmail(email).orElse(new Peserta());
            if (checkEmailPeserta.getEmail()!= null && !Objects.equals(id, checkEmailPeserta.getId())) {
                result.setMessage("Error: Email is already in use!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            Peserta checkNamaPeserta = pesertaRepository.findByNamaPeserta(namaPeserta).orElse(new Peserta());
            if (checkNamaPeserta.getNamaPeserta() != null && !Objects.equals(id, checkNamaPeserta.getId())) {
                result.setMessage("Error: Username is already taken!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (!validator.isPhoneValid(noHp)) {
                result.setMessage("Error: invalid phone number!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            Peserta checkStatusPeserta = pesertaRepository.findById(id).orElse(new Peserta());
            if (checkStatusPeserta.getStatusPeserta() != null && !Objects.equals(EnumStatusPeserta.PESERTA, checkStatusPeserta.getStatusPeserta())) {
                result.setMessage("Error: id: " + id + " is not Peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            Peserta peserta = new Peserta(namaPeserta, tanggalLahir, jenisKelamin, pendidikanTerakhir,noHp, email,
                    alamatRumah, motivasi, kodeReferal);

            peserta.setId(id);
            peserta.setStatusPeserta(EnumStatusPeserta.PESERTA);

            //set kelas
            if (!kelasRepository.findById(kelasId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("cannot find kelas");
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
                result.setMessage("cannot find batch");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setBatch(batchRepository.findById(batchId).get());
            }

            //set image
            if (uploadImage!=null) {
                peserta.setUploadImage(IOUtils.toByteArray(uploadImage.getInputStream()));
            }

            //set provinsi
            if (!provinsiRepository.findById(provinsiId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("cannot find provinsi");
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
                result.setMessage("cannot find kota");
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
                result.setMessage("cannot find kecamatan");
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
                result.setMessage("cannot find kelurahan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setKelurahan(kelurahanRepository.findById(kelurahanId).get());
            }

            pesertaRepository.save(peserta);

            result.setMessage(id == 0 ? "Peserta registered successfully!" : "Peserta updated successfully!");
            result.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> deletePeserta(boolean banned, Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("cannot find peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
                result.setSuccess(false);
                result.setMessage("id: "+ id + " is not peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                pesertaRepository.deletePeserta(banned, id);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> changeToCalonPeserta(Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("cannot find peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
                result.setSuccess(false);
                result.setMessage("id: "+ id + " is not Peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                pesertaRepository.statusPeserta(EnumStatusPeserta.CALON, id);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> changeKelas(Long pesertaId, Long kelasId, String uri) {
        result = new Result();
        try {
            Peserta peserta = pesertaRepository.findById(pesertaId).get();
            Kelas kelas = kelasRepository.findById(kelasId).get();
            if (!pesertaRepository.findById(pesertaId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("cannot find peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (!kelasRepository.findById(kelasId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("cannot find kelas");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
                result.setSuccess(false);
                result.setMessage("id: "+ pesertaId + " is not peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                peserta.setKelas(kelas);
                pesertaRepository.save(peserta);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public Result searchPeserta(String keyword) {
        result = new Result();
        try {
            Map items = new HashMap();
            items.put("items", pesertaRepository.search(keyword,EnumStatusPeserta.PESERTA));
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
            Peserta peserta = new Peserta();
            peserta.setStatusPeserta(EnumStatusPeserta.PESERTA);
            Example<Peserta> example = Example.of(peserta);
            if (ascending) {
                Page<Peserta> pagePeserta = pesertaRepository.findAll(example,
                        PageRequest.of(page, size, Sort.by("namaPeserta").ascending()));
                items.put("items", pagePeserta);
            }
            else {
                Page<Peserta> pagePeserta = pesertaRepository.findAll(example,
                        PageRequest.of(page, size, Sort.by("namaPeserta").descending()));
                items.put("items", pagePeserta);
            }
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }
}
