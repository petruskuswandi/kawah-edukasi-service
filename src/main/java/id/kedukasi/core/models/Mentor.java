package id.kedukasi.core.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "mentors",
      uniqueConstraints = {
  @UniqueConstraint(columnNames = "nama_mentor"),
  @UniqueConstraint(columnNames = "kode")
})
@DynamicUpdate
public class Mentor implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 100)
  private String nama_mentor;

  @NotBlank
  @Size(max = 20)
  private String kode;

  private String no_ktp;

  private String no_telepon;

  @Lob
  @Column(name = "foto")
  @ApiModelProperty(hidden = true)
  private byte[] foto;

  @Lob
  @Column(name = "cv")
  @ApiModelProperty(hidden = true)
  private byte[] cv;


  private String status;


  private String class_name;


  private String pendidikan_univ;


  private String pendidikan_jurusan;

  @Column(name = "tgl_start")
  @Temporal(TemporalType.TIMESTAMP)
  private Date tgl_start;

  @Column(name = "tgl_stop")
  @Temporal(TemporalType.TIMESTAMP)
  private Date tgl_stop;


  private String alamat_rumah;


  private int provinsi;


  private int kota;


  private int kecamatan;

  private int kelurahan;

  @Column(name = "banned", updatable = false)
  private boolean banned;

  @Column(name = "banned_time", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date banned_time;

  @Column(name = "created_time", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date created_time;

  private String created_by;

  @Column(name = "updated_time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updated_time;

  public Mentor() {
  }

  public Mentor(String nama_mentor, String kode, String no_ktp,
                String no_telepon, String status, String class_name, String pendidikan_univ,
                String pendidikan_jurusan, Date tgl_start, Date tgl_stop,  String alamat_rumah) {
    Date date = new Date();

    this.nama_mentor = nama_mentor;
    this.kode = kode;
    this.no_ktp = no_ktp;
    this.no_telepon = no_telepon;
    this.status = status;
    this.class_name = class_name;
    this.pendidikan_univ = pendidikan_univ;
    this.pendidikan_jurusan = pendidikan_jurusan;
    this.tgl_start = tgl_start;
    this.tgl_stop = tgl_stop;
    this.alamat_rumah = alamat_rumah;
    this.banned = false;
    this.banned_time = date;
    this.created_time = date;
    this.updated_time = date;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNama_mentor() {
    return nama_mentor;
  }

  public void setNama(String nama_mentor) {
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

  public byte[] getFoto() {
    return foto;
  }

  public void setFoto(byte[] foto) {
    this.foto = foto;
  }

  public byte[] getCv() {
    return cv;
  }

  public void setCv(byte[] cv) {
    this.cv = cv;
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

  public boolean isBanned() {
    return banned;
  }

  public void setBanned(boolean banned) {
    this.banned = banned;
  }

  public Date getBanned_time() {
    return banned_time;
  }

  public void setBanned_time(Date banned_time) {
    this.banned_time = banned_time;
  }

  public Date getCreated_time() {
    return created_time;
  }

  public void setCreated_time(Date created_time) {
    this.created_time = created_time;
  }

  public String getCreated_by() {
    return created_by;
  }

  public void setCreated_by(String created_by) {
    this.created_by = created_by;
  }

  public Date getUpdated_time() {
    return updated_time;
  }

  public void setUpdated_time(Date updated_time) {
    this.updated_time = updated_time;
  }
}
