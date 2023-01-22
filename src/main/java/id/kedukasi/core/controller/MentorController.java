package id.kedukasi.core.controller;

import javax.servlet.http.HttpServletRequest;
// import javax.validation.Valid;


import id.kedukasi.core.models.Educations;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.User;
import id.kedukasi.core.models.wilayah.MasterKecamatan;
import id.kedukasi.core.models.wilayah.MasterKelurahan;
import id.kedukasi.core.models.wilayah.MasterKota;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Date;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.service.MentorService;
import id.kedukasi.core.utils.StringUtil;

@CrossOrigin
@RestController
@RequestMapping("/mentor")
// @PreAuthorize("hasRole('ROLE_ADMIN')")
public class MentorController {

  @Autowired
  MentorService service;

  @Autowired
  StringUtil stringUtil;

  @Autowired
  HttpServletRequest request;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
  public Result getMentorData(@RequestParam(required = false, name = "search") String search,
                             @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                             @RequestParam(value = "offset", defaultValue = "0") Integer page) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);
    return service.getMentorData(uri, search, limit, page);
  }

  @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
  public Result getMentorByid(@PathVariable("id") long id) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);
    return service.getMentorById(id, uri);
  }

  @PostMapping("/create")
  public ResponseEntity<Result> createMentor(
          @RequestParam(value = "Nama Mentor", required = false) String nama_mentor,
          @RequestPart(value = "Upload Image", required = false) MultipartFile foto,
          @RequestParam(value = "No Ktp", required = false) String no_ktp,
          @RequestParam(value = "No Telepon", required = false) String no_telepon,
          @RequestParam(value = "Status", defaultValue = "Apply", required = false) String status,
          @RequestParam(value = "Class Name by Class Id", required = false) Kelas class_id,
          @RequestParam(value = "Pendidikan Terakhir by Education Id", required = false) Educations pendidikan_terakhir,
          @RequestParam(value = "Pendidikan Jurusan", required = false) String pendidikan_jurusan,
          @RequestParam(value = "Tanggal Start", defaultValue = "2022-05-17", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date tgl_start,
          @RequestParam(value = "Tanggal Stop", defaultValue = "2022-08-17", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date tgl_stop,
          @RequestParam(value = "Alamat Rumah", required = false) String alamat_rumah,
          @RequestPart(value = "Upload Cv", required = false) MultipartFile cv,
          @RequestParam(value = "Provinsi", required = false) MasterProvinsi provinsi,
          @RequestParam(value = "Kota", required = false) MasterKota kota,
          @RequestParam(value = "Kecamatan", required = false) MasterKecamatan kecamatan,
          @RequestParam(value = "Kelurahan", required = false) MasterKelurahan kelurahan,
          @RequestParam(value = "Created by User Id", required = false) Integer userId)
  {
    Long id = 0L;
    return service.createMentor(id, nama_mentor, foto, no_ktp, no_telepon, status,
            class_id, pendidikan_terakhir, pendidikan_jurusan, tgl_start,
            tgl_stop, alamat_rumah, cv, provinsi, kota, kecamatan, kelurahan, userId);
  }

  @PutMapping("/update")
  public ResponseEntity<Result> updateMentor(@RequestParam(value = "Id",defaultValue = "0") Long id,
                                        @RequestParam(value = "Nama Mentor", required = false) String nama_mentor,
                                        @RequestPart(value = "Upload Image", required = false) MultipartFile foto,
                                        @RequestParam(value = "No Ktp", required = false) String no_ktp,
                                        @RequestParam(value = "No Telepon", required = false) String no_telepon,
                                        @RequestParam(value = "Status", defaultValue = "Apply", required = false) String status,
                                        @RequestParam(value = "Class Name by Class Id", required = false) Kelas class_id,
                                        @RequestParam(value = "Pendidikan Terakhir by Education Id", required = false) Educations pendidikan_terakhir,
                                        @RequestParam(value = "Pendidikan Jurusan", required = false) String pendidikan_jurusan,
                                        @RequestParam(value = "Tanggal Start", defaultValue = "2022-05-17", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date tgl_start,
                                        @RequestParam(value = "Tanggal Stop", defaultValue = "2022-08-17", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date tgl_stop,
                                        @RequestParam(value = "Alamat Rumah",required = false) String alamat_rumah,
                                        @RequestPart(value = "Upload Cv", required = false) MultipartFile cv,
                                        @RequestParam(value = "Provinsi",defaultValue = "0", required = false) MasterProvinsi provinsi,
                                        @RequestParam(value = "Kota",defaultValue = "0", required = false) MasterKota kota,
                                        @RequestParam(value = "Kecamatan",defaultValue = "0", required = false) MasterKecamatan kecamatan,
                                        @RequestParam(value = "Kelurahan",defaultValue = "0", required = false) MasterKelurahan kelurahan,
                                        @RequestParam(value = "Created By User ID", defaultValue = "0", required = false) Integer userId) {
    return service.updateMentor(id, nama_mentor, foto, no_ktp, no_telepon, status,
            class_id, pendidikan_terakhir, pendidikan_jurusan, tgl_start,
            tgl_stop, alamat_rumah, cv, provinsi, kota, kecamatan, kelurahan, userId);
  }

  @PatchMapping(value = "/delete")
  public ResponseEntity<Result> deleteMentor(
          @RequestParam(value = "id", defaultValue = "0", required = true) long id,
          @RequestParam(value = "banned", defaultValue = "true") boolean banned
  ) {

    String uri = stringUtil.getLogParam(request);
    logger.info(uri);
    return service.deleteMentor(banned, id, uri);
  }

}
