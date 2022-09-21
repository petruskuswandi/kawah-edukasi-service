package id.kedukasi.core.service;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import id.kedukasi.core.models.Result;

public interface MentorService {
  
    ResponseEntity<?> updateMentor(Long id, String nama_mentor, String kode,MultipartFile foto, String no_ktp,
    String no_telepon, String status, String class_name, String pendidikan_univ,
    String pendidikan_jurusan, Date tgl_start, Date tgl_stop,  String alamat_rumah, MultipartFile cv, Integer provinsi, Integer kota,
    Integer kecamatan, Integer kelurahan);
    
    ResponseEntity<?> deleteMentor(boolean banned, long id, String uri);

    // ResponseEntity<?> updateCvBlob(long id, MultipartFile cv, String uri);
    
    // ResponseEntity<?> updateFotoBlob(long id, MultipartFile foto, String uri);

    Result getAllMentor(String uri);

    Result getMentorById(long id, String uri);
}
