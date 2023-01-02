package id.kedukasi.core.serviceImpl;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import id.kedukasi.core.models.*;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
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
    StringUtil stringUtil;

    @Autowired
    GlobalUtil globalUtil;

    private Result result;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String generatekode(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMyyyy");
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int jumlahdata = mentorRepository.jumlahmentor(year);
        String generateKode = ("M"+dtf.format(now)+ String.format("%03d", jumlahdata+1));
        return generateKode;
    }


    @Override
    public ResponseEntity<?> updateMentor(Long id , String namamentor, MultipartFile foto, String noktp,
                                          String no_telepon, String status, Kelas classID, String pendidikan_univ,
                                          String pendidikan_jurusan, Date tgl_start, Date tgl_stop, String alamat_rumah,
                                          MultipartFile cv, MasterProvinsi provinsiId, MasterKota kotaId, MasterKecamatan kecamatanId, MasterKelurahan kelurahanId, User userID) {
        result = new Result();
        try {
            Mentor checkNamaMentor = mentorRepository.findByNamamentor(namamentor).orElse(new Mentor());
//            Mentor checkKtpMentor = mentorRepository.findByNoktp(noktp).orElse(new Mentor());
            if (checkNamaMentor.getNamamentor() != null && !Objects.equals(id, checkNamaMentor)) {
                result.setMessage("Error: Nama Mentor tidak boleh sama!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

//            if (checkKtpMentor.getNoktp() != null && !Objects.equals(id, checkKtpMentor)) {
//                result.setMessage("Error: KTP Mentor tidak boleh sama!");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }

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

            if(no_telepon.length() < 10 && no_telepon.length() >= 13 || no_telepon.isBlank()) {
                result.setMessage("Error: No Telepon tidak boleh kosong dan harus kurang dari 12 characters");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(status.isBlank()) {
                result.setMessage("Error: Status tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(pendidikan_univ.isBlank()) {
                result.setMessage("Error: Universitas tidak boleh kosong");
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


            Mentor mentor = new Mentor(namamentor, noktp, no_telepon, status, pendidikan_univ,
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
                mentor.setClass_name(classID);
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

            result.setMessage(id == 0 ? "Mentor registered successfully!" : "Mentor updated successfully!");
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
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        result.setMessage("Sukses Menghapus Mentor");
        return ResponseEntity.ok(result);
    }

    @Override
    public Result getMentorById(long id, String uri) {
        result = new Result();
        try {
            Mentor mentor = mentorRepository.findById(id);
            if (mentor == null) {
                result.setSuccess(false);
                result.setMessage("cannot find mentor");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", mentorRepository.findById(id));
                result.setData(items);
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return result;
    }

    @Override
    public Result getMentorData(String uri, String search, Integer limit, Integer page) {
        result = new Result();

        int jumlahpage = (int) Math.ceil(mentorRepository.count() /(double) limit);

        if (limit < 1) {
            limit = 1;
        }

        if (page > jumlahpage) {
            page = jumlahpage;
        }

        if (page < 1 ) {
            page = 1;
        }

        if (search == null) {
            search = "";
        }
        try {
            Map items = new HashMap();
            List<Mentor> batch = mentorRepository.findMentorData(search, limit, page.intValue());
            items.put("items", batch);
            items.put("totalDataResult", batch.size());
            items.put("totalData", mentorRepository.count());

            if (batch.size() == 0) {
                result.setMessage("Maaf Data Mentor yang Anda cari tidak tersedia");
            }
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        result.setMessage("Berhasil Menemukan Mentor Data");
        return result;
    }

}
