package id.kedukasi.core.service;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import id.kedukasi.core.models.Result;

public interface MentorService {

    ResponseEntity<?> updateMentor(Long id,String namamentor, MultipartFile foto, String noktp,
                                   String no_telepon, String status, Long class_name, String pendidikan_univ,
                                   String pendidikan_jurusan, Date tgl_start, Date tgl_stop,  String alamat_rumah, MultipartFile cv, Long provinsi, Long kota,
                                   Long kecamatan, Long kelurahan);

    ResponseEntity<?> deleteMentor(boolean banned, long id, String uri);

    Result getMentorById(long id, String uri);

    Result getMentorData(String uri, String search, Integer limit, Integer page);
}
