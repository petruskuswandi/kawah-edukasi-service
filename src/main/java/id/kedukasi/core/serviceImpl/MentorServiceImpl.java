package id.kedukasi.core.serviceImpl;


import id.kedukasi.core.models.*;
import id.kedukasi.core.models.wilayah.MasterKecamatan;
import id.kedukasi.core.models.wilayah.MasterKelurahan;
import id.kedukasi.core.models.wilayah.MasterKota;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
import id.kedukasi.core.repository.MentorRepository;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.service.MentorService;
import id.kedukasi.core.utils.PathGeneratorUtil;
import id.kedukasi.core.utils.StringUtil;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static id.kedukasi.core.utils.FileUploadUtil.saveFile;

@Service
public class MentorServiceImpl implements MentorService{

    @Autowired
    MentorRepository mentorRepository;
    @Autowired
    StringUtil stringUtil;

    private Result result;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserRepository userRepository;

    @Value("${app.url.staging}")
    String baseUrl;

    private String generatekode(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMyyyy");
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int jumlahdata = mentorRepository.jumlahmentor(year);
        String kode = ("M"+dtf.format(now) + String.format("%03d", jumlahdata+1));
        String generateKode = kode;
        while (mentorRepository.cekkode(generateKode) > 0) {
            generateKode = ("M"+dtf.format(now) + String.format("%03d", jumlahdata++));
        }
        return generateKode;
    }

    @Override
    public ResponseEntity<Result> updateMentor(Long id , String namamentor, MultipartFile foto, String noktp,
                                               String no_telepon, String status, Kelas classID, Educations educationID,
                                               String pendidikan_jurusan, Date tgl_start, Date tgl_stop, String alamat_rumah,
                                               MultipartFile cv, MasterProvinsi provinsiId, MasterKota kotaId, MasterKecamatan kecamatanId, MasterKelurahan kelurahanId, Integer userId) {
        result = new Result();
        try {
            if(namamentor == null) {
                result.setMessage("Error: Name can't be empty/null!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(noktp == null || noktp.length() != 16) {
                result.setSuccess(false);
                result.setMessage("Error: No KTP harus sama dengan 16 character dan tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(no_telepon.length() < 11 || no_telepon.length() > 14 || no_telepon == null) {
                result.setMessage("Error: Number phone can't be empty/null and must be less than 12 numbers!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(status == null) {
                result.setMessage("Error: Status can't be empty/null");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(pendidikan_jurusan == null) {
                result.setMessage("Error: Major can't be empty/null");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(alamat_rumah == null) {
                result.setMessage("Error: Alamat tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            if (tgl_start == null) {
                result.setMessage("Error : Start date is null/empty");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (tgl_stop == null) {
                result.setMessage("Error : Ended Time is null/empty");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (tgl_start.after(tgl_stop)){
                result.setMessage("Error : Start date tidak boleh lebih besar dari end date");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            Mentor mentor = new Mentor(namamentor, noktp, no_telepon, status,
                    pendidikan_jurusan, tgl_start, tgl_stop, alamat_rumah);

            mentor.setId(id);
            mentor.setKode(mentorRepository.ambilkode(id));

            //set kelas
            if (classID == null) {
                result.setSuccess(false);
                result.setMessage("Kelas tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setClass_id(classID);
            }
            if (educationID == null){
                result.setSuccess(false);
                result.setMessage("Pendidikan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setPendidikan_terakhir(educationID);
            }

            //Set created_by Many to one User
            Optional<User> user = Optional.ofNullable(userRepository.findById(userId));
            if (user == null) {
                result.setSuccess(false);
                result.setMessage("User tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setCreated_by(user.get());
            }


            //set foto
            if (foto!=null) {
                String fileFotoName = StringUtils.cleanPath(foto.getOriginalFilename());
                String fileCode = saveFile(fileFotoName, foto);
                mentor.setFoto(PathGeneratorUtil.generate(fileCode, baseUrl));
            }

            //set cv
            if (cv!=null) {
                String fileCvName = StringUtils.cleanPath(cv.getOriginalFilename());
                String fileCode = saveFile(fileCvName, cv);
                mentor.setCv(PathGeneratorUtil.generate(fileCode, baseUrl));
            }

            //set provinsi
            if (provinsiId == null) {
                result.setSuccess(false);
                result.setMessage("Provinsi tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setProvinsi(provinsiId);
            }

            //set kota
            if (kotaId == null) {
                result.setSuccess(false);
                result.setMessage("Kota tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setKota(kotaId);
            }

            //set kecamatan
            if (kecamatanId == null) {
                result.setSuccess(false);
                result.setMessage("Kecamatan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setKecamatan(kecamatanId);
            }

            //set kelurahan
            if (kelurahanId == null) {
                result.setSuccess(false);
                result.setMessage("Kelurahan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setKelurahan(kelurahanId);
            }

            if (id!=0) {
                Date date = new Date();
                mentor.setUpdated_time(date);
            } else {
                mentor.setCreated_time(null);
                mentor.setBanned_time(null);
            }

            mentorRepository.save(mentor);

            result.setSuccess(true);
            result.setMessage("Mentor updated successfully!");
            result.setCode(HttpStatus.OK.value());

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> createMentor(Long id , String namamentor, MultipartFile foto, String noktp,
                                               String no_telepon, String status, Kelas classID, Educations educationID,
                                               String pendidikan_jurusan, Date tgl_start, Date tgl_stop, String alamat_rumah,
                                               MultipartFile cv, MasterProvinsi provinsiId, MasterKota kotaId, MasterKecamatan kecamatanId, MasterKelurahan kelurahanId, Integer userId) {
        result = new Result();
        try {
            if(namamentor == null) {
                result.setSuccess(false);
                result.setMessage("Error: Name can't be empty/null!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(noktp == null || noktp.length() != 16) {
                result.setSuccess(false);
                result.setMessage("Error: No KTP harus sama dengan 16 character dan tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if (no_telepon == null || no_telepon != null && no_telepon.length() < 11 || no_telepon.length() > 13 && no_telepon != null) {
                result.setSuccess(false);
                result.setMessage("Error: No Telepon tidak boleh kosong dan No Telepon tidak boleh lebih dari 13 dan kurang dari 10");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(status == null) {
                result.setSuccess(false);
                result.setMessage("Error: Status tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(pendidikan_jurusan == null) {
                result.setSuccess(false);
                result.setMessage("Error: Jurusan tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(alamat_rumah == null) {
                result.setSuccess(false);
                result.setMessage("Error: Alamat tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }


            Mentor mentor = new Mentor(namamentor, noktp, no_telepon, status,
                    pendidikan_jurusan, tgl_start, tgl_stop, alamat_rumah);

            mentor.setId(id);
            mentor.setKode(generatekode());

            //set kelas
            if (classID == null) {
                result.setSuccess(false);
                result.setMessage("Kelas tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setClass_id(classID);
            }

            if (educationID == null){
                result.setSuccess(false);
                result.setMessage("Pendidikan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setPendidikan_terakhir(educationID);
            }

            if (userId == null) {
                result.setSuccess(false);
                result.setMessage("User tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            //Set created_by Many to one User;
            Optional<User> userID = Optional.ofNullable(userRepository.findById(userId));
            if (userID.isEmpty() || userID.get().isBanned()) {
                result.setSuccess(false);
                result.setMessage("User tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setCreated_by(userID.get());
            }

            //set foto
            if (foto!=null) {
                String fileFotoName = StringUtils.cleanPath(foto.getOriginalFilename());
                String fileCode = saveFile(fileFotoName, foto);
                mentor.setFoto(PathGeneratorUtil.generate(fileCode, baseUrl));
            }

            //set cv
            if (cv!=null) {
                String fileCvName = StringUtils.cleanPath(cv.getOriginalFilename());
                String fileCode = saveFile(fileCvName, cv);
                mentor.setCv(PathGeneratorUtil.generate(fileCode, baseUrl));
            }

            //set provinsi
            if (provinsiId == null) {
                result.setSuccess(false);
                result.setMessage("Provinsi tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setProvinsi(provinsiId);
            }

            //set kota
            if (kotaId == null) {
                result.setSuccess(false);
                result.setMessage("Kota tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setKota(kotaId);
            }

            //set kecamatan
            if (kecamatanId == null) {
                result.setSuccess(false);
                result.setMessage("Kecamatan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setKecamatan(kecamatanId);
            }

            //set kelurahan
            if (kelurahanId == null) {
                result.setSuccess(false);
                result.setMessage("Kelurahan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setKelurahan(kelurahanId);
            }

            if (id!=0) {
                Date date = new Date();
                mentor.setCreated_time(date);
            } else {
                mentor.setUpdated_time(null);
                mentor.setBanned_time(null);
            }
            if (tgl_start == null) {
                result.setMessage("Error : Start date is null/empty");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (tgl_stop == null) {
                result.setMessage("Error : Ended Time is null/empty");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (tgl_start.after(tgl_stop)){
                result.setMessage("Error : Start date tidak boleh lebih besar dari end date");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            mentorRepository.save(mentor);

            result.setSuccess(true);
            result.setMessage("Mentor registered successfully!");
            result.setCode(HttpStatus.OK.value());

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> deleteMentor(boolean banned, long id, String uri) {
        result = new Result();
        try {
            mentorRepository.deleteMentor(banned, id);
            if (mentorRepository.existsById(id)){
                result.setMessage(banned == true ? "Success delete mentor" : "Success Backup mentor");
            } else {
                result.setCode(HttpStatus.BAD_REQUEST.value());
                result.setMessage("Id its not found");
                result.setSuccess(false);
            }
        } catch (Exception e) {

            logger.error(stringUtil.getError(e));
            result.setSuccess(false);
            result.setMessage(e.getCause().getCause().getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public Result getMentorById(long id, String uri) {
        result = new Result();
        try {
            Mentor mentor = mentorRepository.findById(id);
            if (mentor == null) {
                result.setSuccess(false);
                result.setMessage("Cannot find id mentor");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", mentorRepository.findById(id));
                result.setData(items);
                result.setMessage("Success find id mentor");
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return result;
    }

    @Override
    public Result getMentorData(String uri, String search, Integer limit, Integer page) {
        result = new Result();

        if (search == null) {
            search = "";
        }
        List<Mentor> mentor = null;
        try {
            Map items = new HashMap();
            mentor = mentorRepository.findMentorData(search.toLowerCase(), limit, page.intValue());
            items.put("items", mentor);
            items.put("totalDataResult", mentor.size());
            items.put("totalData", mentorRepository.bannedfalse());

            if (mentor.size() == 0 || limit > mentorRepository.bannedfalse()) {
                result.setCode(HttpStatus.BAD_REQUEST.value());
                result.setSuccess(false);
                result.setData(limit > mentorRepository.bannedfalse() ? 0 : mentor.size());
                result.setMessage(mentor.size() != 0 ? "Sorry limit exceeds size data mentor" : "Sorry data mentor is null");
            } else if (mentor == null) {
                result.setCode(HttpStatus.BAD_REQUEST.value());
                result.setSuccess(false);
                result.setData(null);
                result.setMessage("Sorry data mentor is null");
            } else {
                result.setData(items);
                result.setMessage("Success find Data Mentor");
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return result;
    }

}
