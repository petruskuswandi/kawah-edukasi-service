package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.enums.EnumStatusTes;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.PesertaRepository;
import id.kedukasi.core.request.PesertaRequest;
import id.kedukasi.core.service.FilesStorageService;
import id.kedukasi.core.service.PesertaService;
import id.kedukasi.core.utils.GlobalUtil;
import id.kedukasi.core.utils.StringUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PesertaServiceImpl implements PesertaService {

    @Autowired
    PesertaRepository pesertaRepository;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    GlobalUtil globalUtil;

    @Autowired
    FilesStorageService storageService;

    private Result result;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public Result getAllPeserta(String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            items.put("items", pesertaRepository.findAll());
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
                result.setMessage("cannot find user");
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
    public ResponseEntity<?> updatePeserta(PesertaRequest pesertaRequest) {
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

            Peserta peserta = new Peserta(pesertaRequest.getNamaPeserta(), pesertaRequest.getTanggalLahir(),
                    pesertaRequest.getJenisKelamin(), pesertaRequest.getPendidikanTerakhir(), pesertaRequest.getNoHp(),
                    pesertaRequest.getEmail(), pesertaRequest.getProvinsi(),pesertaRequest.getKota(),
                    pesertaRequest.getKecamatan(), pesertaRequest.getKelurahan(),pesertaRequest.getAlamatRumah(),
                    pesertaRequest.getMotivasi(), pesertaRequest.getKodeReferal());

            peserta.setId(pesertaRequest.getId());
            pesertaRepository.save(peserta);

            result.setMessage(pesertaRequest.getId() == 0 ? "User registered successfully!" : "User updated successfully!");
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
            pesertaRepository.deletePeserta(banned, id);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> statusPeserta(Long statusPesertaOrd, long id, String uri) {
        result = new Result();
        try {
            if (statusPesertaOrd==0) {
                pesertaRepository.statusPeserta(EnumStatusPeserta.CALON, id);
            } else if (statusPesertaOrd==1) {
                pesertaRepository.statusPeserta(EnumStatusPeserta.PESERTA, id);
            } else {
                result.setMessage("Error: Use 0 for Calon dan 1 for Peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> statusTes(Long statusTesOrd, long id, String uri) {
        result = new Result();
        try {
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
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> updateUploadImageBlob(long id, MultipartFile profilePicture, String uri) {
        result = new Result();
        try {
            pesertaRepository.updateUploadImage(IOUtils.toByteArray(profilePicture.getInputStream()), id);
        } catch (IOException e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> updateUploadImageFolder(long id, MultipartFile profilePicture, String uri) {
        result = new Result();
        try {
            String filename = String.valueOf("uploadImage" + id).concat(".")
                    .concat(globalUtil.getExtensionByStringHandling(profilePicture.getOriginalFilename()).orElse(""));
            String filenameResult = storageService.save(profilePicture, filename);
            pesertaRepository.setUploadImagePath(filenameResult, id);
            result.setMessage("succes to save file ".concat(profilePicture.getOriginalFilename()));
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
