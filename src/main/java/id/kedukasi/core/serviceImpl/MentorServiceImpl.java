package id.kedukasi.core.serviceImpl;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import id.kedukasi.core.models.*;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
import id.kedukasi.core.repository.EducationRepository;
import id.kedukasi.core.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import id.kedukasi.core.models.wilayah.MasterKecamatan;
import id.kedukasi.core.models.wilayah.MasterKelurahan;
import id.kedukasi.core.models.wilayah.MasterKota;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.MentorRepository;
import id.kedukasi.core.repository.wilayah.KecamatanRepository;
import id.kedukasi.core.repository.wilayah.KelurahanRepository;
import id.kedukasi.core.repository.wilayah.KotaRepository;
import id.kedukasi.core.repository.wilayah.ProvinsiRepository;
import id.kedukasi.core.service.MentorService;
import id.kedukasi.core.utils.GlobalUtil;
import id.kedukasi.core.utils.StringUtil;
import org.w3c.dom.ranges.Range;

import java.util.stream.IntStream;

@Service
public class MentorServiceImpl implements MentorService{

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    UserRepository userRepository;

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
    EducationRepository educationRepository;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    GlobalUtil globalUtil;

    private Result result;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    LocalDateTime now = LocalDateTime.now();
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
    public ResponseEntity<?> updateMentor(Long id , String namamentor, MultipartFile foto, String noktp,
                                          String no_telepon, String status, Kelas classID, Educations educationID,
                                          String pendidikan_jurusan, Date tgl_start, Date tgl_stop, String alamat_rumah,
                                          MultipartFile cv, MasterProvinsi provinsiId, MasterKota kotaId, MasterKecamatan kecamatanId, MasterKelurahan kelurahanId, User userID) {
        result = new Result();
        try {
            if(namamentor.isBlank()) {
                result.setMessage("Error: Name can't be empty/null!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(noktp.length() < 16 || noktp.isBlank()) {
                result.setMessage("Error: Number KTP can't be empty/null and must be less than 16 characters!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(no_telepon.length() < 10 && no_telepon.length() >= 13 || no_telepon.isBlank()) {
                result.setMessage("Error: Number phone can't be empty/null and must be less than 12 numbers!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(status.isBlank()) {
                result.setMessage("Error: Status can't be empty/null");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(pendidikan_jurusan.isBlank()) {
                result.setMessage("Error: Major can't be empty/null");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(alamat_rumah.isBlank()) {
                result.setMessage("Error: Alamat tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }


            Mentor mentor = new Mentor(namamentor, noktp, no_telepon, status,
                    pendidikan_jurusan, tgl_start, tgl_stop, alamat_rumah);

            mentor.setId(id);
            mentor.setKode(mentorRepository.ambilkode(id));

            //set kelas
            if (!kelasRepository.findById(classID.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Kelas tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setClass_id(classID);
            }
            if (!educationRepository.findById(educationID.getId()).isPresent()){
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
            if (!userRepository.findById(userID.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("User tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setCreated_by(userID);
            }

            //set foto
            if (foto!=null) {
                mentor.setFoto(IOUtils.toByteArray(foto.getInputStream()));
            }

            //set cv
            if (cv!=null) {
                mentor.setCv(IOUtils.toByteArray(cv.getInputStream()));
            }

            //set provinsi
            if (!provinsiRepository.findById(provinsiId.getId()).isPresent()) {
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
            MasterKota kota = kotaRepository.findById(kotaId.getId()).get();
            if (kota == null) {
                result.setSuccess(false);
                result.setMessage("Kota tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                mentor.setKota(kotaId);
            }

            //set kecamatan
            MasterKecamatan kecamatan = kecamatanRepository.findById(kecamatanId.getId()).get();
            if (kecamatan == null) {
                result.setSuccess(false);
                result.setMessage("Kecamatan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                mentor.setKecamatan(kecamatanId);
            }

            //set kelurahan
            MasterKelurahan kelurahan = kelurahanRepository.findById(kelurahanId.getId()).get();
            if (kelurahan == null) {
                result.setSuccess(false);
                result.setMessage("Kelurahan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                mentor.setKelurahan(kelurahanId);
            }

            if (id!=0) {
                Date date = new Date();
                mentor.setUpdated_time(date);
            }

            mentorRepository.save(mentor);

            result.setMessage("Mentor updated successfully!");
            result.setCode(HttpStatus.OK.value());

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> createMentor(Long id , String namamentor, MultipartFile foto, String noktp,
                                          String no_telepon, String status, Kelas classID, Educations educationID,
                                          String pendidikan_jurusan, Date tgl_start, Date tgl_stop, String alamat_rumah,
                                          MultipartFile cv, MasterProvinsi provinsiId, MasterKota kotaId, MasterKecamatan kecamatanId, MasterKelurahan kelurahanId, User userID) {
        result = new Result();
        try {
            if(namamentor.isBlank()) {
                result.setMessage("Error: Nama tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(noktp.length() < 16 || noktp.isBlank()) {
                result.setMessage("Error: No KTP tidak boleh kosong dan harus kurang dari 16 character");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(no_telepon.length() < 10 || no_telepon.length() > 13 || no_telepon.isBlank()) {
                result.setMessage("Error: No Telepon tidak boleh kosong dan harus kurang dari 13 characters");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(status.isBlank()) {
                result.setMessage("Error: Status tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(pendidikan_jurusan.isBlank()) {
                result.setMessage("Error: Jurusan tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(alamat_rumah.isBlank()) {
                result.setMessage("Error: Alamat tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }


            Mentor mentor = new Mentor(namamentor, noktp, no_telepon, status,
                    pendidikan_jurusan, tgl_start, tgl_stop, alamat_rumah);

            mentor.setId(id);
            mentor.setKode(generatekode());

            //set kelas
            if (!kelasRepository.findById(classID.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Kelas tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setClass_id(classID);
            }

            if (!educationRepository.findById(educationID.getId()).isPresent()){
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
            if (!userRepository.findById(userID.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("User tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setCreated_by(userID);
            }

            //set foto
            if (foto!=null) {
                mentor.setFoto(IOUtils.toByteArray(foto.getInputStream()));
            }

            //set cv
            if (cv!=null) {
                mentor.setCv(IOUtils.toByteArray(cv.getInputStream()));
            }

            //set provinsi
            if (!provinsiRepository.findById(provinsiId.getId()).isPresent()) {
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
            MasterKota kota = kotaRepository.findById(kotaId.getId()).get();
            if (kota == null) {
                result.setSuccess(false);
                result.setMessage("Kota tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                mentor.setKota(kotaId);
            }

            //set kecamatan
            MasterKecamatan kecamatan = kecamatanRepository.findById(kecamatanId.getId()).get();
            if (kecamatan == null) {
                result.setSuccess(false);
                result.setMessage("Kecamatan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                mentor.setKecamatan(kecamatanId);
            }

            //set kelurahan
            MasterKelurahan kelurahan = kelurahanRepository.findById(kelurahanId.getId()).get();
            if (kelurahan == null) {
                result.setSuccess(false);
                result.setMessage("Kelurahan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                mentor.setKelurahan(kelurahanId);
            }

            if (id!=0) {
                Date date = new Date();
                mentor.setUpdated_time(date);
            }

            mentorRepository.save(mentor);

            result.setMessage("Mentor registered successfully!");
            result.setCode(HttpStatus.OK.value());

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> deleteMentor(boolean banned, long id, String uri) {
        result = new Result();
        try {
            mentorRepository.deleteMentor(banned, id);
            if (mentorRepository.existsById(id)){
                result.setMessage(banned == true ? "Success delete mentor" : "Success Backup mentor");
            } else {
                result.setCode(400);
                result.setMessage("Id its not found");
                result.setSuccess(false);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
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
                result.setMessage("Cannot find id mentor" + id);
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

        int jumlahpage = (int) Math.ceil(mentorRepository.count() / (double) limit);

        if (limit < 1) {
            limit = 1;
        }

        if (page > jumlahpage) {
            page = jumlahpage;
        }

        if (page < 1) {
            page = 1;
        }

        if (search == null) {
            search = "";
        }
        List<Mentor> batch = null;
        try {
            Map items = new HashMap();
            batch = mentorRepository.findMentorData(search, limit, page.intValue());
            items.put("items", batch);
            items.put("totalDataResult", batch.size());
            items.put("totalData", mentorRepository.count());

            if (batch.size() == 0) {
                result.setCode(400);
                result.setSuccess(false);
                result.setData(batch.size());
                result.setMessage("Sorry Data its null/empty");
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
