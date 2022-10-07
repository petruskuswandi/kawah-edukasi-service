package id.kedukasi.core.controller;

import javax.servlet.http.HttpServletRequest;
// import javax.validation.Valid;

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
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class MentorController {

@Autowired
  MentorService service;

  @Autowired
  StringUtil stringUtil;

  @Autowired
  HttpServletRequest request;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
  public Result getAll() {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);
    return service.getAllMentor(uri);
  }

  @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
  public Result getMentorByid(@PathVariable("id") long id) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);
    return service.getMentorById(id, uri);
  }

  @PostMapping("/create")
  public ResponseEntity<?> createMentor(
  @RequestParam(value = "Nama Mentor") String nama_mentor,
  @RequestParam(value = "Kode Mentor") String kode,
  @RequestPart(value = "Upload Image", required = false) MultipartFile foto,
  @RequestParam(value = "No Ktp") String no_ktp,
  @RequestParam(value = "No Telepon") String no_telepon,
  @RequestParam(value = "Status") String status,
  @RequestParam(value = "Class Name") Long class_name,
  @RequestParam(value = "Pendidikan Univ") String pendidikan_univ,
  @RequestParam(value = "Pendidikan Jurusan") String pendidikan_jurusan,
  @RequestParam(value = "Tanggal Start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date tgl_start,
  @RequestParam(value = "Tanggal Stop") @DateTimeFormat(pattern = "yyyy-MM-dd") Date tgl_stop,
  @RequestParam(value = "Alamat Rumah") String alamat_rumah,
  @RequestPart(value = "Upload Cv", required = false) MultipartFile cv,
  @RequestParam(value = "Provinsi") Long provinsi,
  @RequestParam(value = "Kota") Long kota,
  @RequestParam(value = "Kecamatan") Long kecamatan,
  @RequestParam(value = "Kelurahan") Long kelurahan )
  {
  Long id = 0L;
  return service.updateMentor(id, nama_mentor, kode, foto, no_ktp, no_telepon, status,
                                class_name, pendidikan_univ, pendidikan_jurusan, tgl_start,
                                tgl_stop, alamat_rumah, cv, provinsi, kota, kecamatan, kelurahan);
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateMentor(@RequestParam(value = "Id",defaultValue = "0") Long id,
  @RequestParam(value = "Nama Mentor") String nama_mentor,
  @RequestParam(value = "Kode Mentor") String kode,
  @RequestPart(value = "Upload Image", required = false) MultipartFile foto,
  @RequestParam(value = "No Ktp") String no_ktp,
  @RequestParam(value = "No Telepon") String no_telepon,
  @RequestParam(value = "Status") String status,
  @RequestParam(value = "Class Name") Long class_name,
  @RequestParam(value = "Pendidikan Univ") String pendidikan_univ,
  @RequestParam(value = "Pendidikan Jurusan") String pendidikan_jurusan,
  @RequestParam(value = "Tanggal Start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date tgl_start,
  @RequestParam(value = "Tanggal Stop") @DateTimeFormat(pattern = "yyyy-MM-dd") Date tgl_stop,
  @RequestParam(value = "Alamat Rumah") String alamat_rumah,
  @RequestPart(value = "Upload Cv", required = false) MultipartFile cv,
  @RequestParam(value = "Provinsi",defaultValue = "0") Long provinsi,
  @RequestParam(value = "Kota",defaultValue = "0") Long kota,
  @RequestParam(value = "Kecamatan",defaultValue = "0") Long kecamatan,
  @RequestParam(value = "Kelurahan",defaultValue = "0") Long kelurahan ) {
    return service.updateMentor(id, nama_mentor, kode, foto, no_ktp, no_telepon, status,
                                class_name, pendidikan_univ, pendidikan_jurusan, tgl_start,
                                tgl_stop, alamat_rumah, cv, provinsi, kota, kecamatan, kelurahan);
  }

  @PatchMapping(value = "/delete")
  public ResponseEntity<?> deleteMentor(
      @RequestParam(value = "id", defaultValue = "0", required = true) long id,
      @RequestParam(value = "banned", defaultValue = "true") boolean banned
  ) {

    String uri = stringUtil.getLogParam(request);
    logger.info(uri);
    return service.deleteMentor(banned, id, uri);
  }

  // @PatchMapping(value = "/updateFotoBlob", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  // public ResponseEntity<?> updateFoto(
  //     @RequestParam(value = "id", defaultValue = "0", required = true) long id,
  //     @RequestPart("foto") MultipartFile foto) {

  //   String uri = stringUtil.getLogParam(request);
  //   logger.info(uri);
  //   return service.updateFotoBlob(id, foto, uri);
  // }

  // @PatchMapping(value = "/updateCvBlob", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  // public ResponseEntity<?> updateCv(
  //     @RequestParam(value = "id", defaultValue = "0", required = true) long id,
  //     @RequestPart("cv") MultipartFile cv) {

  //   String uri = stringUtil.getLogParam(request);
  //   logger.info(uri);
  //   return service.updateCvBlob(id, cv, uri);
  // }
}
