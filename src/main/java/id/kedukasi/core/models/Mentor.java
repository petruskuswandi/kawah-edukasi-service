package id.kedukasi.core.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import id.kedukasi.core.models.wilayah.MasterKecamatan;
import id.kedukasi.core.models.wilayah.MasterKelurahan;
import id.kedukasi.core.models.wilayah.MasterKota;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "mentors", uniqueConstraints = {
        @UniqueConstraint(columnNames = "kode")
})
@Data
@AllArgsConstructor
@DynamicUpdate
@Getter
@Setter
public class Mentor implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(name = "namamentor")
  @Size(max = 100)
  @Column(name = "namamentor")
  private String namamentor;

  @NotBlank
  @Column(name = "kode")
  @Size(max = 20)
  @Column(name = "kode")
  private String kode;

  @NotBlank
  @Column(name = "noktp")
  @Size(max = 16)
  @Column(name = "noktp")
  private String noktp;

  @Column(name = "no_telepon")
  private String no_telepon;

  @Lob
  @Column(name = "foto")
  @ApiModelProperty(hidden = true)
  private byte[] foto;

  @Lob
  @Column(name = "cv")
  @ApiModelProperty(hidden = true)
  private byte[] cv;

  @Column(name = "status")
  private String status;

  @JsonIgnoreProperties({"description", "banned", "banned_time", "banned_time", "created_by", "created_time", "updated_time"})
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "class_id")
  private Kelas class_id;


  @ManyToOne(fetch = FetchType.EAGER)
  @JsonIgnoreProperties({"description", "created_at", "updated_at", "deleted"})
  @JoinColumn(name = "pendidikan_terakhir")
  private Educations pendidikan_terakhir;

  @Column(name = "pendidikan_jurusan")
  private String pendidikan_jurusan;

  @Column(name = "tgl_start")
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
  @Temporal(TemporalType.DATE)
  private Date tgl_start;

  @Column(name = "tgl_stop")
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
  @Temporal(TemporalType.DATE)
  private Date tgl_stop;

  @Column(name = "alamat_rumah")
  private String alamat_rumah;


  @ManyToOne
  @JsonIgnoreProperties({"alt_name", "latitude", "longitude"})
  @JoinColumn(name = "provinsi")
  private MasterProvinsi provinsi;


  @ManyToOne
  @JsonIgnoreProperties({"alt_name", "latitude", "longitude", "provinsi_id", "province_id"})
  @JoinColumn(name = "kota")
  private MasterKota kota;


  @ManyToOne
  @JsonIgnoreProperties({"alt_name", "latitude", "longitude", "kota_id"})
  @JoinColumn(name = "kecamatan")
  private MasterKecamatan kecamatan;

  @ManyToOne
  @JsonIgnoreProperties({"alt_name", "latitude", "longitude", "kota_id", "kecamatan_id"})
  @JoinColumn(name = "kelurahan")
  private MasterKelurahan kelurahan;

  @Column(name = "banned", updatable = false)
  private boolean banned;

  @Column(name = "banned_time", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(pattern = "yyyy-MM-dd,  HH:mm:ss", timezone = "Asia/Jakarta")
  private Date banned_time;

  @Column(name = "created_time", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "Asia/Jakarta")
  private Date created_time;

  @JsonIgnoreProperties({"profilePicture","profilePicturePath","email","password","namaLengkap","noHp","role","isLogin","isActive","tokenVerification","created_time","updated_time","banned","banned_time","verified"})
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "created_by")
  private User created_by;

  @Column(name = "updated_time")
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "Asia/Jakarta")
  private Date updated_time;

  public Mentor() {
  }

  public Mentor(String namamentor, String noktp, String no_telepon, String status,
                String pendidikan_jurusan, Date tgl_start, Date tgl_stop,  String alamat_rumah) {
    Date date = new Date();
    this.namamentor = namamentor;
    this.noktp = noktp;
    this.no_telepon = no_telepon;
    this.status = status;
    this.pendidikan_jurusan = pendidikan_jurusan;
    this.tgl_start = tgl_start;
    this.tgl_stop = tgl_stop;
    this.alamat_rumah = alamat_rumah;
    this.banned = false;
    this.banned_time = date;
    this.created_time = date;
    this.updated_time = date;
  }
}
