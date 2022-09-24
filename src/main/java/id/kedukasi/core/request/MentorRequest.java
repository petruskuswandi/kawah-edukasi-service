package id.kedukasi.core.request;

import java.io.Serializable;
import java.util.Date;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModelProperty;

public class MentorRequest implements Serializable {

  private Long id;

  @NotBlank
  @Size(max = 100)
  @ApiModelProperty(example = "yanto", required = true)
  private String nama_mentor;

  @NotBlank
  @Size(min = 3,max = 20)
  @ApiModelProperty(example = "M303", required = true)
  private String kode;

  @NotBlank
  @Size(min = 16,max = 20)
  @ApiModelProperty(example = "32022219", required = true)
  private String no_ktp;

  @NotBlank
  @Size(min = 12,max = 20)
  @ApiModelProperty(example = "081235454", required = true)
  private String no_telepon;


  @ApiModelProperty(example = "Apply,", required = true)
  private String status;

  @NotBlank
  @Size(min = 2,max = 15)
  @ApiModelProperty(example = "Back-End", required = true)
  private String class_name;

  
  @ApiModelProperty(example = "Universitas Padjadjaran", required = true)
  private String pendidikan_univ;

  
  @ApiModelProperty(example = "Teknik Informatika", required = true)
  private String pendidikan_jurusan;

 
  @ApiModelProperty(example = "2022-05-17", required = true)
  private Date tgl_start;

  
  @ApiModelProperty(example = "2022-08-17", required = true)
  private Date tgl_stop;

  
  
  @ApiModelProperty(example = "jl.mencari cinta", required = true)
  private String alamat_rumah;


  @ApiModelProperty(example = "1", required = true)
  private int provinsi;

 
  @ApiModelProperty(example = "1", required = true)
  private int kota;


  @ApiModelProperty(example = "1", required = true)
  private int kecamatan;


  @ApiModelProperty(example = "1", required = true)
  private int kelurahan;

  public Long getId() {
    return id;
  }


  public void setId(Long id) {
    this.id = id;
  }

  public String getNama_mentor() {
    return nama_mentor;
  }

  public void setNama_mentor(String nama_mentor) {
    this.nama_mentor = nama_mentor;
  }

  public String getKode() {
    return kode;
  }

  public void setKode(String kode) {
    this.kode = kode;
  }

  public String getNo_ktp() {
    return no_ktp;
  }

  public void setNo_ktp(String no_ktp) {
    this.no_ktp = no_ktp;
  }

  public String getNo_telepon() {
    return no_telepon;
  }

  public void setNo_telepon(String no_telepon) {
    this.no_telepon = no_telepon;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getClass_name() {
    return class_name;
  }

  public void setClass_name(String class_name) {
    this.class_name = class_name;
  }

  public String getPendidikan_univ() {
    return pendidikan_univ;
  }

  public void setPendidikan_univ(String pendidikan_univ) {
    this.pendidikan_univ = pendidikan_univ;
  }

  public String getPendidikan_jurusan() {
    return pendidikan_jurusan;
  }

  public void setPendidikan_jurusan(String pendidikan_jurusan) {
    this.pendidikan_jurusan = pendidikan_jurusan;
  }

  public Date getTgl_start() {
    return tgl_start;
  }

  public void setTgl_start(Date tgl_start) {
    this.tgl_start = tgl_start;
  }

  public Date getTgl_stop() {
    return tgl_stop;
  }

  public void setTgl_stop(Date tgl_stop) {
    this.tgl_stop = tgl_stop;
  }

  public String getAlamat_rumah() {
    return alamat_rumah;
  }

  public void setAlamat_rumah(String alamat_rumah) {
    this.alamat_rumah = alamat_rumah;
  }

  public Integer getProvinsi() {
    return provinsi;
  }

  public void setProvinsi(Integer provinsi) {
    this.provinsi = provinsi;
  }

  public Integer getKota() {
    return kota;
  }

  public void setKota(Integer kota) {
    this.kota = kota;
  }

  public Integer getKecamatan() {
    return kecamatan;
  }

  public void setKecamatan(Integer kecamatan) {
    this.kecamatan = kecamatan;
  }

  public Integer getKelurahan() {
    return kelurahan;
  }

  public void setKelurahan(Integer kelurahan) {
    this.kelurahan = kelurahan;
  }
}
