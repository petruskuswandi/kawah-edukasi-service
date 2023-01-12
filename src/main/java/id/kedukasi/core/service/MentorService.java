package id.kedukasi.core.service;

import java.util.Date;

import id.kedukasi.core.models.Educations;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.User;
import id.kedukasi.core.models.wilayah.MasterKecamatan;
import id.kedukasi.core.models.wilayah.MasterKelurahan;
import id.kedukasi.core.models.wilayah.MasterKota;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import id.kedukasi.core.models.Result;

public interface MentorService {

    ResponseEntity<?> updateMentor(Long id, String namamentor, MultipartFile foto, String noktp,
                                   String no_telepon, String status, Kelas class_id, Educations pendidikan_terakhir,
                                   String pendidikan_jurusan, Date tgl_start, Date tgl_stop, String alamat_rumah, MultipartFile cv, MasterProvinsi provinsi, MasterKota kota,
                                   MasterKecamatan kecamatan, MasterKelurahan kelurahan, User created_by);

    ResponseEntity<?> createMentor(Long id, String namamentor, MultipartFile foto, String noktp,
                                   String no_telepon, String status, Kelas class_id, Educations pendidikan_terakhir,
                                   String pendidikan_jurusan, Date tgl_start, Date tgl_stop, String alamat_rumah, MultipartFile cv, MasterProvinsi provinsi, MasterKota kota,
                                   MasterKecamatan kecamatan, MasterKelurahan kelurahan, User created_by);

    ResponseEntity<?> deleteMentor(boolean banned, long id, String uri);

    Result getMentorById(long id, String uri);

    Result getMentorData(String uri, String search, Integer limit, Integer page);
}
