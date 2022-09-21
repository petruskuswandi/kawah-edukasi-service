package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.enums.EnumStatusTes;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class CalonPesertaServiceImpl implements CalonPesertaService {

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
    public Result getAllCalonPeserta(String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            Peserta calonPeserta = new Peserta();
            calonPeserta.setStatusPeserta(EnumStatusPeserta.CALON);
            Example<Peserta> example = Example.of(calonPeserta);
            items.put("items", pesertaRepository.findAll(example));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getCalonPesertaById(long id, String uri) {
        result = new Result();
        try {
            Peserta peserta = pesertaRepository.findById(id);
            if (peserta == null) {
                result.setSuccess(false);
                result.setMessage("cannot find calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("id: "+ id + " is not calon peserta");
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
    public ResponseEntity<?> updateCalonPeserta(Long id, Long kelasId, String namaPeserta, Date tanggalLahir,
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
            if (checkStatusPeserta.getStatusPeserta() != null && !Objects.equals(EnumStatusPeserta.CALON, checkStatusPeserta.getStatusPeserta())) {
                result.setMessage("Error: id: " + id + " is not Calon Peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            Peserta peserta = new Peserta(namaPeserta, tanggalLahir, jenisKelamin, pendidikanTerakhir,noHp, email,
                    alamatRumah, motivasi, kodeReferal);

            peserta.setId(id);
            peserta.setStatusPeserta(EnumStatusPeserta.CALON);

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

            result.setMessage(id == 0 ? "Calon peserta registered successfully!" : "Calon peserta updated successfully!");
            result.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> deleteCalonPeserta(boolean banned, long id, String uri) {
        result = new Result();
        try {
            Peserta peserta = pesertaRepository.findById(id);
            if (peserta == null) {
                result.setSuccess(false);
                result.setMessage("cannot find calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("id: "+ id + " is not calon peserta");
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
    public ResponseEntity<?> changeToPeserta(long id, String uri) {
        result = new Result();
        try {
            Peserta peserta = pesertaRepository.findById(id);
            if (peserta == null) {
                result.setSuccess(false);
                result.setMessage("cannot find calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("id: "+ id + " is not calon peserta");
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
    public ResponseEntity<?> changeStatusTes(Long statusTesOrd, long id, String uri) {
        result = new Result();
        try {
            Peserta peserta = pesertaRepository.findById(id);
            if (peserta == null) {
                result.setSuccess(false);
                result.setMessage("cannot find calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("id: "+ id + " is not calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                if (statusTesOrd==0) {
                    pesertaRepository.statusTes(EnumStatusTes.LULUS, id);
                } else if (statusTesOrd==1) {
                    pesertaRepository.statusTes(EnumStatusTes.MELAKSANAKANTES, id);
                } else if (statusTesOrd==2) {
                    pesertaRepository.statusTes(EnumStatusTes.MENUNGGUFOLLOWUP, id);
                } else {
                    result.setMessage("Error: Use 0 for Lulus, 1 for Melaksanakan Tes and 2 for Menunggu Follow Up");
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
    public ResponseEntity<?> changeKelas(long calonPesertaId, long kelasId, String uri) {
        result = new Result();
        try {
            Peserta calonPeserta = pesertaRepository.findById(calonPesertaId);
            Kelas kelas = kelasRepository.findById(kelasId);
            if (calonPeserta == null) {
                result.setSuccess(false);
                result.setMessage("cannot find peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (kelas == null) {
                result.setSuccess(false);
                result.setMessage("cannot find kelas");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (calonPeserta.getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("id: "+ calonPesertaId + " is not calon peserta");
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
                items.put("items", pesertaRepository.findAll(example));
                result.setData(items);
            } else if (statusTesOrd==1) {
                peserta.setStatusTes(EnumStatusTes.MENUNGGUFOLLOWUP);
                peserta.setStatusPeserta(EnumStatusPeserta.CALON);
                Example<Peserta> example = Example.of(peserta);
                items.put("items", pesertaRepository.findAll(example));
                result.setData(items);
            } else if (statusTesOrd==2) {
                peserta.setStatusTes(EnumStatusTes.LULUS);
                peserta.setStatusPeserta(EnumStatusPeserta.CALON);
                Example<Peserta> example = Example.of(peserta);
                items.put("items", pesertaRepository.findAll(example));
                result.setData(items);
            } else {
                result.setMessage("Error: Use 0 for Melaksanakan Tes, 1 for Menunggu Follow Up dan 2 for Lulus");
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
