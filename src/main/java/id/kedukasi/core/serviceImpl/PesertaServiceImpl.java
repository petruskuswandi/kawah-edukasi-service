package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.PesertaRepository;
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
    public ResponseEntity<?> updatePeserta(PesertaRequest pesertaRequest, long kelasId, MultipartFile uploadImage) {
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
            peserta.setStatusPeserta(EnumStatusPeserta.PESERTA);

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

            result.setMessage(pesertaRequest.getId() == 0 ? "Peserta registered successfully!" : "Peserta updated successfully!");
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
