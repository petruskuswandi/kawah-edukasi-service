package id.kedukasi.core.serviceImpl;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import id.kedukasi.core.models.Mentor;
import id.kedukasi.core.models.Result;
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


    @Override
    public ResponseEntity<?> updateMentor(Long id ,String nama_mentor, String kode, MultipartFile foto, String no_ktp,
                                          String no_telepon, String status, Long classID, String pendidikan_univ,
                                          String pendidikan_jurusan, Date tgl_start, Date tgl_stop,  String alamat_rumah,
                                          MultipartFile cv, Long provinsiId, Long kotaId, Long kecamatanId, Long kelurahanId) {
        result = new Result();
        try {
            Mentor checkKodeMentor = mentorRepository.findByKode(kode).orElse(new Mentor());
            if (checkKodeMentor.getKode() != null && !Objects.equals(id, checkKodeMentor.getId())) {
                result.setMessage("Error: Kode mentor harus uniq!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if(nama_mentor.isBlank()) {
                result.setMessage("Error: Nama tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(kode.isBlank()) {
                result.setMessage("Error: Kode tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(no_ktp.length() < 16 || no_ktp.isBlank()) {
                result.setMessage("Error: No KTP tidak boleh kosong dan harus kurang dari 16 character");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(no_telepon.length() < 12 || no_telepon.isBlank()) {
                result.setMessage("Error: No Telepon tidak boleh kosong dan harus kurang dari 12 characters");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(no_telepon.length() < 12 || no_telepon.isBlank()) {
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


            Mentor mentor = new Mentor(nama_mentor, kode, no_ktp, no_telepon, status, pendidikan_univ,
                    pendidikan_jurusan, tgl_start, tgl_stop, alamat_rumah);

            mentor.setId(id);

            //set kelas
            if (!kelasRepository.findById(classID).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Kelas tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                mentor.setClass_name(classID);
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
            if (!provinsiRepository.findById(provinsiId).isPresent()) {
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
            MasterKota kota = kotaRepository.findById(kotaId).get();
            if (kota == null) {
                result.setSuccess(false);
                result.setMessage("Kota tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                mentor.setKota(kotaId);
            }

            //set kecamatan
            MasterKecamatan kecamatan = kecamatanRepository.findById(kecamatanId).get();
            if (kecamatan == null) {
                result.setSuccess(false);
                result.setMessage("Kecamatan tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                mentor.setKecamatan(kecamatanId);
            }

            //set kelurahan
            MasterKelurahan kelurahan = kelurahanRepository.findById(kelurahanId).get();
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

        return ResponseEntity.ok(result);
    }

    @Override
    public Result getAllMentor(String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            items.put("items", mentorRepository.findAll());
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return result;
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

}
