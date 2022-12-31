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
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "mentors")
@DynamicUpdate
@Getter
@Setter
public class Mentor implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 100)
  private String namamentor;

  @NotBlank
  @Size(max = 20)
  private String kode;

  @NotBlank
  @Size(max = 16)
  private String noktp;

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


  private Long class_name;


  private String pendidikan_univ;


  private String pendidikan_jurusan;

  @Column(name = "tgl_start")
  @Temporal(TemporalType.TIMESTAMP)
  private Date tgl_start;

  @Column(name = "tgl_stop")
  @Temporal(TemporalType.TIMESTAMP)
  private Date tgl_stop;


  private String alamat_rumah;


  private Long provinsi;


  private Long kota;


  private Long kecamatan;

  private Long kelurahan;

  @Column(name = "banned", updatable = false)
  private boolean banned;

  @Column(name = "banned_time", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date banned_time;

  @Column(name = "created_time", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date created_time;

  private long created_by;

  @Column(name = "updated_time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updated_time;

  public Mentor() {
  }

  public Mentor(String namamentor, String noktp,
                String no_telepon, String status, String pendidikan_univ,
                String pendidikan_jurusan, Date tgl_start, Date tgl_stop,  String alamat_rumah) {
    Date date = new Date();

    this.namamentor = namamentor;
    this.noktp = noktp;
    this.no_telepon = no_telepon;
    this.status = status;
    this.pendidikan_univ = pendidikan_univ;
    this.pendidikan_jurusan = pendidikan_jurusan;
    this.tgl_start = tgl_start;
    this.tgl_stop = tgl_stop;
    this.alamat_rumah = alamat_rumah;
    this.banned = false;
    this.banned_time = date;
    this.created_by = 1;
    this.created_time = date;
    this.updated_time = date;
  }
}
