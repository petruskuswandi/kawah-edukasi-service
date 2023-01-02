package id.kedukasi.core.request;

import java.io.Serializable;
import java.util.Date;


import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorRequest implements Serializable {

  private Long id;

  @NotBlank
  @Size(max = 100)
  @ApiModelProperty(example = "yanto", required = true)
  private String namamentor;

  private String kode;

  @NotBlank
  @Size(min = 16,max = 20)
  @ApiModelProperty(example = "32022219", required = true)
  private String noktp;

  @NotBlank
  @Size(min = 12,max = 20)
  @ApiModelProperty(example = "081235454", required = true)
  private String no_telepon;


  @ApiModelProperty(example = "Apply,", required = true)
  private String status;

  @NotBlank
  @Size(min = 2,max = 15)
  @ApiModelProperty(example = "Back-End", required = true)
  @ManyToOne
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
}
