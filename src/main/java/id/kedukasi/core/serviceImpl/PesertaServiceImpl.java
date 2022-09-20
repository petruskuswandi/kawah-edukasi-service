package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.wilayah.MasterKecamatan;
import id.kedukasi.core.models.wilayah.MasterKelurahan;
import id.kedukasi.core.models.wilayah.MasterKota;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.PesertaRepository;
import id.kedukasi.core.repository.wilayah.KecamatanRepository;
import id.kedukasi.core.repository.wilayah.KelurahanRepository;
import id.kedukasi.core.repository.wilayah.KotaRepository;
import id.kedukasi.core.repository.wilayah.ProvinsiRepository;
import id.kedukasi.core.request.PesertaRequest;
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
    public Result getPesertaById(long id, String uri) {
        result = new Result();
        try {
            Peserta peserta = pesertaRepository.findById(id);
            if (peserta == null) {
                result.setSuccess(false);
                result.setMessage("cannot find peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
                result.setSuccess(false);
                result.setMessage("id: "+ id + " is not peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", pesertaRepository.findById(id));
                result.setData(items);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public ResponseEntity<?> updatePeserta(Long id, Long kelasId, String namaPeserta, Date tanggalLahir,
                                           String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
                                           MultipartFile uploadImage, Integer provinsiId, Integer kotaId, Integer kecamatanId,
                                           Integer kelurahanId, String alamatRumah, String motivasi, String kodeReferal) {
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
            if (checkStatusPeserta.getStatusPeserta() != null && !Objects.equals(EnumStatusPeserta.PESERTA, checkNamaPeserta.getStatusPeserta())) {
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
            Kelas kelas = kelasRepository.findById(kelasId).get();
            if (kelas == null) {
                result.setSuccess(false);
                result.setMessage("cannot find kelas");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                peserta.setKelas(kelas);
            }

            //set image
            if (uploadImage!=null) {
                peserta.setUploadImage(IOUtils.toByteArray(uploadImage.getInputStream()));
            }

            //set provinsi
            MasterProvinsi provinsi = provinsiRepository.findById(provinsiId).get();
            if (provinsi == null) {
                result.setSuccess(false);
                result.setMessage("cannot find provinsi");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                peserta.setProvinsi(provinsi);
            }

            //set kota
            MasterKota kota = kotaRepository.findById(kotaId).get();
            if (kota == null) {
                result.setSuccess(false);
                result.setMessage("cannot find kota");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                peserta.setKota(kota);
            }

            //set kecamatan
            MasterKecamatan kecamatan = kecamatanRepository.findById(kecamatanId).get();
            if (kecamatan == null) {
                result.setSuccess(false);
                result.setMessage("cannot find kecamatan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                peserta.setKecamatan(kecamatan);
            }

            //set kelurahan
            MasterKelurahan kelurahan = kelurahanRepository.findById(kelurahanId).get();
            if (kelurahan == null) {
                result.setSuccess(false);
                result.setMessage("cannot find kelurahan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                peserta.setKelurahan(kelurahan);
            }

            if (id!=0) {
                Date date = new Date();
                peserta.setUpdated_time(date);
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
    public ResponseEntity<?> deletePeserta(boolean banned, long id, String uri) {
        result = new Result();
        try {
            Peserta peserta = pesertaRepository.findById(id);
            if (peserta == null) {
                result.setSuccess(false);
                result.setMessage("cannot find peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
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
    public ResponseEntity<?> changeToCalonPeserta(long id, String uri) {
        result = new Result();
        try {
            Peserta peserta = pesertaRepository.findById(id);
            if (peserta == null) {
                result.setSuccess(false);
                result.setMessage("cannot find peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
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
    public ResponseEntity<?> changeKelas(long pesertaId, long kelasId, String uri) {
        result = new Result();
        try {
            Peserta peserta = pesertaRepository.findById(pesertaId);
            Kelas kelas = kelasRepository.findById(kelasId);
            if (peserta == null) {
                result.setSuccess(false);
                result.setMessage("cannot find peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (kelas == null) {
                result.setSuccess(false);
                result.setMessage("cannot find kelas");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
                result.setSuccess(false);
                result.setMessage("id: "+ peserta + " is not peserta");
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

    @Override
    public Result cekNoHP(String noHp) {
        result = new Result();
        try {
            if (validator.isPhoneValid(noHp)) {
                result.setMessage("valid");
            }
            else {
                result.setMessage("invalid");
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }
}
