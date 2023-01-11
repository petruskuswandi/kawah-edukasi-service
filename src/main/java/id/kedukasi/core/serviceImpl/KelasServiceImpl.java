package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.*;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.request.KelasRequest;
import id.kedukasi.core.request.UpdateKelasRequest;
import id.kedukasi.core.service.KelasService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KelasServiceImpl implements KelasService {

    @Autowired
    KelasRepository kelasRepository;

    @Autowired
    StringUtil stringUtil;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Override
    public Result getAllClass(String uri, String search, long limit, long offset) {
        result = new Result();
        int jumlahpage = (int) Math.ceil(kelasRepository.count() /(double) limit);

        if (limit == -99) {
            limit = kelasRepository.count();}

//        if (offset > jumlahpage) {
//            offset = jumlahpage;
//        }

        if (offset == -99 ) {
            offset= 0;
        }

        if (search == null) {
            search = "";
        }
        try {
            Map items = new HashMap();
            List<Kelas> kelas = kelasRepository.findKelasData( search,false, limit, offset);
            items.put("items", kelas);
            items.put("totalDataResult", kelas.size());
            items.put("totalData", kelasRepository.countKelasData(false));
            if (kelas.size() == 0) {
                result.setMessage("Maaf Data Kelas yang Anda cari tidak tersedia");
            }
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
            items.put("items", kelasRepository.findAll(example, Sort.by(Sort.Direction.ASC, "id")));
            result.setData(items);

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

//    @Override
//    public Result getAllBatchByKelas(long idKelas) {
//        result = new Result();
//        try {
//            Map<String, List<Kelas>> items = new HashMap<>();
//            Kelas kelas = new Kelas();
//            Example<Kelas> example = Example.of(kelas);
//            items.put("items", kelasRepository.findAll(example, Sort.by(Sort.Direction.ASC,"id")));
//            result.setData(items);
//        } catch (Exception e) {
//            logger.error(stringUtil.getError(e));
//        }
//        return ResponseEntity.ok(result).getBody();

//        Optional<Kelas> kelas = kelasRepository.findById(idKelas);
//        if(!kelas.isPresent()){
//            result.setCode(404);
//            result.setMessage("Kelas tidak ada");
//            return result;
//        }
//        List<Batch> batch = kelasRepository.getAllBatch(kelas.get());
//        result.setCode(200);
//        result.setMessage("Berhasil Ambil Batch");
//        result.setData(batch);
//        return result;
//    }

    @Override
    public Result getClassById(Long id, String uri) {
        result = new Result();
        Optional<Kelas> kelas = kelasRepository.findById(id);
        try {
            if (!kelasRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kelas dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }if (kelas.get().isBanned() == true) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " is banned");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }
            else {
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
            items.put("items", kelasRepository.findAll(example, Sort.by(Sort.Direction.ASC, "id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public ResponseEntity<Result> updateClass(UpdateKelasRequest Request) {
        result = new Result();
        try {
            Kelas checkClassname = kelasRepository.findByClassname(Request.getClassName()).orElse(new Kelas());
            if (checkClassname.getClassname()!= null && !Objects.equals(Request.getId(), checkClassname.getId())) {
                result.setMessage("Error: Nama kelas sudah digunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            if (!kelasRepository.findById(Request.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kelas dengan id " + Request.getId());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }
            if(Request.getClassName().length()<3
                    || Request.getClassName().length()>20
                    || Request.getClassName().isBlank()) {
                result.setMessage("Error: Nama kelas tidak boleh kosong dan harus terdiri dari 3-20 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            if(Request.getDescription().length()>50 || Request.getDescription().isBlank()) {
                result.setMessage("Error: Deskripsi kelas tidak boleh kosong dan harus kurang dari 50 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            Optional<User> user = userRepository.findById(Request.getCreated_by());
            if (!user.isPresent()){
                result.setSuccess(false);
                result.setMessage("Error: id User tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                User user_id = userRepository.findById(Request.getCreated_by()).get();
                Request.setCreated_by(user_id.getId());
            }

//                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            Kelas kelasbaru = new Kelas(Request.getClassName(), Request.getDescription(), auth.getName());
            Kelas kelasbaru = new Kelas(Request.getId(), Request.getClassName(), Request.getDescription());
            kelasbaru.setCreated_by(user.get());
            kelasRepository.save(kelasbaru);

            result.setMessage(Request.getId() == 0 ? "Berhasil membuat kelas baru!" : "Berhasil memperbarui kelas!");
            result.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }
    @Override
    public ResponseEntity<Result> createClass(KelasRequest Request) {
        result = new Result();
        try {
            Kelas checkClassname = kelasRepository.findByClassname(Request.getClassName()).orElse(new Kelas());
            if (checkClassname.getClassname()!= null) {
                result.setMessage("Error: Nama kelas sudah digunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            if(Request.getClassName().length()<3
                    || Request.getClassName().length()>20
                    || Request.getClassName().isBlank()) {
                result.setMessage("Error: Nama kelas tidak boleh kosong dan harus terdiri dari 3-20 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            if(Request.getDescription().length()>50 || Request.getDescription().isBlank()) {
                result.setMessage("Error: Deskripsi kelas tidak boleh kosong dan harus kurang dari 50 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            Optional<User> user = Optional.ofNullable(userRepository.findById(Request.getCreated_by()));
            if (!user.isPresent()){
                result.setSuccess(false);
                result.setMessage("Error: id User tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                User user_id = userRepository.findById(Request.getCreated_by());
                Request.setCreated_by(Math.toIntExact(user_id.getId()));
            }

//                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            Kelas kelasbaru = new Kelas(Request.getClassName(), Request.getDescription(), auth.getName());
            Kelas kelasbaru = new Kelas(Request.getClassName(), Request.getDescription());
            kelasbaru.setCreated_by(user.get());
            kelasRepository.save(kelasbaru);

            result.setMessage("Berhasil membuat kelas baru!");
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
