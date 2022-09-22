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
import id.kedukasi.core.models.wilayah.Kecamatan;
import id.kedukasi.core.models.wilayah.Kelurahan;
import id.kedukasi.core.models.wilayah.Kota;
import id.kedukasi.core.models.wilayah.Provinsi;
import id.kedukasi.core.repository.MentorRepository;
import id.kedukasi.core.repository.wilayah.KecamatanRepository;
import id.kedukasi.core.repository.wilayah.KelurahanRepository;
import id.kedukasi.core.repository.wilayah.KotaRepository;
import id.kedukasi.core.repository.wilayah.ProvinsiRepository;
import id.kedukasi.core.request.MentorRequest;
import id.kedukasi.core.service.MentorService;
import id.kedukasi.core.utils.GlobalUtil;
import id.kedukasi.core.utils.StringUtil;

@Service
public class MentorServiceImpl implements MentorService{

    @Autowired
    MentorRepository mentorRepository;

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
    public ResponseEntity<?> updateMentor(Long id, String nama_mentor, String kode, MultipartFile foto, String no_ktp,
                                          String no_telepon, String status, String class_name, String pendidikan_univ,
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

        if(no_ktp.length() < 16 || no_ktp.length() > 20) {
          result.setMessage("Error: No KTP can must be 16-20 characters");
          result.setCode(HttpStatus.BAD_REQUEST.value());
          return ResponseEntity.badRequest().body(result);
      }
      if(no_telepon.length() < 12 || no_telepon.length() > 20) {
          result.setMessage("Error: No KTP can must be 12-20 characters");
          result.setCode(HttpStatus.BAD_REQUEST.value());
          return ResponseEntity.badRequest().body(result);
      }

      if(class_name.length() < 2 || class_name.length() > 15) {
        result.setMessage("Error: Name Class can must be 2-15 characters");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(result);
      }
  
        Mentor mentor = new Mentor(nama_mentor, kode, no_ktp, no_telepon, status, class_name, pendidikan_univ,
                                   pendidikan_jurusan, tgl_start, tgl_stop, alamat_rumah);

        mentor.setId(id);

        if (foto!=null) {
            mentor.setFoto(IOUtils.toByteArray(foto.getInputStream()));
          }

        if (cv!=null) {
          mentor.setCv(IOUtils.toByteArray(cv.getInputStream()));
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
            mentor.setProvinsi(provinsiId);
        }

        //set kota
        Kota kota = kotaRepository.findById(kotaId).get();
        if (kota == null) {
            result.setSuccess(false);
            result.setMessage("cannot find kota");
            result.setCode(HttpStatus.BAD_REQUEST.value());
        } else {
            mentor.setKota(kotaId);
        }

        //set kecamatan
        Kecamatan kecamatan = kecamatanRepository.findById(kecamatanId).get();
        if (kecamatan == null) {
            result.setSuccess(false);
            result.setMessage("cannot find kecamatan");
            result.setCode(HttpStatus.BAD_REQUEST.value());
        } else {
            mentor.setKecamatan(kecamatanId);
        }

        //set kelurahan
        Kelurahan kelurahan = kelurahanRepository.findById(kelurahanId).get();
        if (kelurahan == null) {
            result.setSuccess(false);
            result.setMessage("cannot find kelurahan");
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
