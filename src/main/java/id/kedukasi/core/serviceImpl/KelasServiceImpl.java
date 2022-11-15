package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.Batch;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.request.KelasRequest;
import id.kedukasi.core.service.KelasService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class KelasServiceImpl implements KelasService {

    @Autowired
    KelasRepository kelasRepository;

    @Autowired
    StringUtil stringUtil;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Result getAllClass(String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            Kelas kelas = new Kelas();
            kelas.setBanned(false);
            Example<Kelas> example = Example.of(kelas);
            items.put("items", kelasRepository.findAll(example, Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getAllBannedKelas(String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            Kelas kelas = new Kelas();
            kelas.setBanned(true);
            Example<Kelas> example = Example.of(kelas);
            items.put("items", kelasRepository.findAll(example, Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getClassById(Long id, String uri) {
        result = new Result();
        try {
            if (!kelasRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kelas dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", kelasRepository.findById(id).get());
                result.setData(items);
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }
    public Result getProgramRunning(String uri) {
        result = new Result();
        try {

            Map items = new HashMap();
            Kelas kelas = new Kelas();
            kelas.setBanned(false);
            Example<Kelas> example = Example.of(kelas);
            items.put("items", kelasRepository.findAll(example, Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }
    @Override
    public ResponseEntity<?> updateClass(KelasRequest kelasRequest) {
        result = new Result();
        try {
            Kelas checkClassname = kelasRepository.findByClassname(kelasRequest.getClassname()).orElse(new Kelas());
            if (checkClassname.getClassname()!= null && !Objects.equals(kelasRequest.getId(), checkClassname.getId())) {
                result.setMessage("Error: Nama kelas sudah digunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            if(kelasRequest.getClassname().length()<3
                    || kelasRequest.getClassname().length()>20
                    || kelasRequest.getClassname().isBlank()) {
                result.setMessage("Error: Nama kelas tidak boleh kosong dan harus terdiri dari 3-20 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            if(kelasRequest.getDescription().length()>50 || kelasRequest.getDescription().isBlank()) {
                result.setMessage("Error: Deskripsi kelas tidak boleh kosong dan harus kurang dari 50 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Kelas kelasbaru = new Kelas(kelasRequest.getClassname(), kelasRequest.getDescription(), auth.getName());

            kelasbaru.setId(kelasRequest.getId());
            kelasRepository.save(kelasbaru);

            result.setMessage(kelasRequest.getId() == 0 ? "Berhasil membuat kelas baru!" : "Berhasil memperbarui kelas!");
            result.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> deleteClass(boolean banned, Long id, String uri) {
        result = new Result();
        try {
            if (!kelasRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kelas dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                kelasRepository.deleteKelas(banned, id);
                if (banned) {
                    result.setMessage("Berhasil menghapus kelas");
                } else {
                    result.setMessage("Berhasil mengembalikan kelas");
                }
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }
}
