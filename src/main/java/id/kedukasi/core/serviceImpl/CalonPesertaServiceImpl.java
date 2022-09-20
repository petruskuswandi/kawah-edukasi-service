package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.enums.EnumStatusTes;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.PesertaRepository;
import id.kedukasi.core.request.PesertaRequest;
import id.kedukasi.core.service.CalonPesertaService;
import id.kedukasi.core.service.FilesStorageService;
import id.kedukasi.core.utils.GlobalUtil;
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
    public ResponseEntity<?> updateCalonPeserta(PesertaRequest pesertaRequest, long kelasId, MultipartFile uploadImage) {
        result = new Result();
        try {
            Peserta checkEmailPeserta = pesertaRepository.findByEmail(pesertaRequest.getEmail()).orElse(new Peserta());
            if (checkEmailPeserta.getEmail()!= null && !Objects.equals(pesertaRequest.getId(), checkEmailPeserta.getId())) {
                result.setMessage("Error: Email is already in use!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            Peserta checkNamaPeserta = pesertaRepository.findByNamaPeserta(pesertaRequest.getNamaPeserta()).orElse(new Peserta());
            if (checkNamaPeserta.getNamaPeserta() != null && !Objects.equals(pesertaRequest.getId(), checkNamaPeserta.getId())) {
                result.setMessage("Error: Username is already taken!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (!validator.isPhoneValid(pesertaRequest.getNoHp())) {
                result.setMessage("Error: invalid phone number!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            Peserta peserta = new Peserta(pesertaRequest.getNamaPeserta(), pesertaRequest.getTanggalLahir(),
                    pesertaRequest.getJenisKelamin(), pesertaRequest.getPendidikanTerakhir(), pesertaRequest.getNoHp(),
                    pesertaRequest.getEmail(), pesertaRequest.getProvinsi(),pesertaRequest.getKota(),
                    pesertaRequest.getKecamatan(), pesertaRequest.getKelurahan(),pesertaRequest.getAlamatRumah(),
                    pesertaRequest.getMotivasi(), pesertaRequest.getKodeReferal());

            peserta.setId(pesertaRequest.getId());
            peserta.setStatusPeserta(EnumStatusPeserta.CALON);

            Kelas kelas = kelasRepository.findById(kelasId);
            if (kelas == null) {
                result.setSuccess(false);
                result.setMessage("cannot find kelas");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                peserta.setKelas(kelas);
            }

            if (uploadImage!=null) {
                peserta.setUploadImage(IOUtils.toByteArray(uploadImage.getInputStream()));
            }

            pesertaRepository.save(peserta);

            result.setMessage(pesertaRequest.getId() == 0 ? "Calon peserta registered successfully!" : "Calon peserta updated successfully!");
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
            } else if (statusTesOrd==3) {
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
