package id.kedukasi.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.enums.EnumStatusTes;
import id.kedukasi.core.models.wilayah.MasterKecamatan;
import id.kedukasi.core.models.wilayah.MasterKelurahan;
import id.kedukasi.core.models.wilayah.MasterKota;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "peserta", uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "no_hp"),
                @UniqueConstraint(columnNames = "nomor_ktp"),
})
@DynamicUpdate
public class Peserta implements Serializable {

        @Id
        @GeneratedValue(
                strategy= GenerationType.AUTO,
                generator="native")
        private Long id;

        @JsonIgnoreProperties({"description", "banned","banned_time",
                "startedtime","endedtime","created_by","created_time","updated_time"})
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "batch_id")
        // @NotNull(message = "Data Batch Tidak Boleh Kosong")
        private Batch batch;
//        @ManyToOne(fetch = FetchType.EAGER)
//        @JoinTable(name = "peserta_kelas",
//                joinColumns = @JoinColumn(name = "peserta_id"),
//                inverseJoinColumns = @JoinColumn(name = "kelas_id"))
//        private Kelas kelas;
//
//        @ManyToOne(fetch = FetchType.EAGER)
//        @JoinTable(name = "peserta_batch",
//                joinColumns = @JoinColumn(name = "peserta_id"),
//                inverseJoinColumns = @JoinColumn(name = "batch_id"))
//        private Batch batch;

        // register
        @NotEmpty(message = "Nama Tidak Boleh Kosong")
        @Column(name = "nama_peserta", nullable = false)
        @Size(max = 50, message = "Jumlah Karakter Nama Peserta maksimal 50 karakter ")
        private String namaPeserta;

        @Size(max = 20, message = "Jumlah Karakter No KTP Peserta maksimal 20 karakter ")
        @Column(name = "nomor_ktp")
        private String nomorKtp;

        // register
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "tanggal_lahir", nullable = false)
        @NotNull(message = "Tanggal Lahir Tidak Boleh Kosong")
        private Date tanggalLahir;

        @Column(name = "jenis_kelamin", length = 20)
        @Size(max = 20, message = "Jumlah Karakter Jenis Kelamin maksimal 20 karakter ")
        private String jenisKelamin;

//        @Column(name = "pendidikan_terakhir", length = 20)
//        @Size(max = 20, message = "Jumlah Karakter Pendidikan Terakhir maksimal 20 karakter ")
//        private String pendidikanTerakhir;

        // register
        // @NotEmpty(message = "Np HP Tidak Boleh Kosong")
        @Pattern(regexp = "^(\\+62|62)?[\\s-]?0?8[1-9]{1}\\d{1}[\\s-]?\\d{4}[\\s-]?\\d{2,5}$", message = "Format Hp Tidak Sesuai")
        @Column(name = "no_hp", length = 15)
        private String noHp;
        // register
        // @NotEmpty(message = "Email Tidak Boleh Kosong")
        @Email(message = "Format Email Tidak Sesuai")
        @Size(max = 100, message = "Jumlah Karakter Email maksimal 100 karakter ")
        @Pattern(regexp = "^(?=.{1,64})[A-Za-z0-9_\\-]+(\\.[A-Za-z0-9_\\-]+)*+@[^-]{3,}[A-Za-z0-9-]+(\\.[A-Za-z]{2,})*+$", message = "Format Email Tidak Sesuai")
        private String email;

//        @Lob
//        @Column(name = "upload_image")
//        @ApiModelProperty(hidden = true)
//        private byte[] uploadImage;
//
//        @Lob
//        @Column(name = "upload_cv")
//        @ApiModelProperty(hidden = true)
//        private byte[] uploadCv;

        @Column(name = "uploadImagePath")
        @ApiModelProperty(hidden = true)
        private String uploadImagePath;

        @Column(name = "upload_img_name")
        @ApiModelProperty(hidden = true)
        private String uploadImageName;

        // register
        @JsonIgnoreProperties({"alt_name","latitude","longitude"})
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "provinsi_id", nullable = false)
        @NotNull(message = "Data Provinsi Tidak Boleh Kosong")
        private MasterProvinsi provinsi;

        // register
        @JsonIgnoreProperties({"alt_name","latitude","longitude"})
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "kota_id", nullable = false)
        @NotNull(message = "Data Kota Tidak Boleh Kosong")
        private MasterKota kota;

        // register
        @JsonIgnoreProperties({"alt_name","latitude","longitude"})
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "kecamatan_id", nullable = false)
        @NotNull(message = "Data Kecamatan Tidak Boleh Kosong")
        private MasterKecamatan kecamatan;

        // register
        @JsonIgnoreProperties({"alt_name","latitude","longitude"})
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "kelurahan_id", nullable = false)
        @NotNull(message = "Data Kelurahan Tidak Boleh Kosong")
        private MasterKelurahan kelurahan;

        // register
        @JsonIgnoreProperties({"created_at","updated_at","deleted","description","flag", "subFlag"})
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "status_id", nullable = false)
        @NotNull(message = "Data Status Tidak Boleh Kosong")
        private Status status;

        @JsonIgnoreProperties({"description","created_time","updated_time","created_at","updated_at","deleted"})
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "education_id", nullable = true)
        private Educations tingkat_pendidikan;

        // register
        @NotEmpty(message = "Alamat Tidak Boleh Kosong")
        @Size(max = 255, message = "Jumlah Karakter ALamat maksimal 255 karakter ")
        @Column(name = "alamat_rumah", nullable = false)
        private String alamatRumah;

        @Size(max = 255, message = "Jumlah Karakter Motivasi maksimal 255 karakter ")
        @Column(name = "motivasi")
        private String motivasi;

        @Column(name = "kode_referal")
        private String kodeReferal;

    
        @Enumerated(EnumType.STRING)
        @Column(name = "statusTes", length = 20)
        private EnumStatusTes statusTes;

     
        @Enumerated(EnumType.STRING)
        @Column(name = "statusPeserta", length = 20)
        private EnumStatusPeserta statusPeserta;

        @Column(name = "banned")
        private boolean banned;

        @Column(name = "banned_time")
        @Temporal(TemporalType.TIMESTAMP)
        @JsonFormat(pattern = "yyyy/MM/dd")
        private Date banned_time;

        @CreationTimestamp
        @Column(name = "created_time")
        @Temporal(TemporalType.TIMESTAMP)
        @JsonFormat(pattern = "yyyy/MM/dd")
        private Date created_time;

        @UpdateTimestamp
        @Column(name = "updated_time")
        @Temporal(TemporalType.TIMESTAMP)
        @JsonFormat(pattern = "yyyy/MM/dd")
        private Date updated_time;

        // @ManyToOne(fetch = FetchType.EAGER)
        // @JoinColumn(name = "program_id", nullable = false, updatable = false)
        // @NotNull(message = "Data Program Tidak Boleh Kosong")
        // private Program program;

        /**
         * status tag twitbbon
         */
        // @AssertTrue(message = "status twittbon harus selalu true")
        @Column(name = "stat_twibbon")
        private boolean statTwibbon;

        // @NotEmpty(message = "Link Untuk Twittbon Tidak Boleh Kosong")
        @Column(name = "link_twittbon", length = 50)
        @Size(max = 50, message = "Jumlah Karakter Link Twittbon maksimal 50 karakter ")
        private String linkTwiitbon;
        /**
         * 1. java
         * 2. tidak menguasai
         * 3. other
         */
        // @NotNull(message = "pemrograman int Tidak Boleh Kosong")
        @Column(name = "pemrograman", length = 3)
        private Integer pemrograman;

        @Column(name = "keterangan_pemrograman", length = 255)
        @Size(max = 255, message = "Jumlah Karakter Keterangan Pemrograman maksimal 255 karakter ")
        private String keteranganPemrograman;

        /**
         * mengikuti bootcamp sebelumnya
         */
        @Column(name = "stat_bootcamp")
        private boolean statBootcamp;

        @Column(name = "nama_bootcamp", length = 50)
        @Size(max = 50, message = "Jumlah Karakter nama bootcamp maksimal 50 karakter ")
        private String namaBootcamp;

        // @NotEmpty(message = "Sekolah/Universitas Tidak Boleh Kosong")
        @Column(name = "sekolah_universitas", length = 50)
        @Size(max = 50, message = "Jumlah Karakter Sekolah/Universitas maksimal 50 karakter ")
        private String sekolahUniversitas;

        // @NotEmpty(message = "Jurusan Tidak Boleh Kosong")
        @Size(max = 50, message = "Jumlah Karakter Jurusan maksimal 50 karakter ")
        @Column(name = "jurusan", length = 50)
        private String jurusan;

        // @NotEmpty(message = "Tahun Lulus Tidak Boleh Kosong")
        @Size(max = 4, message = "Jumlah Karakter Tahun Lulus maksimal 4 karakter ")
        @Column(name = "tahun_lulus", length = 4)
        private String tahunLulus;

        // @NotEmpty(message = "User Instagram Tidak Boleh Kosong")
        @Size(max = 50, message = "Jumlah Karakter User Instagram maksimal 50 karakter ")
        @Column(name = "user_instagram", length = 50)
        private String userInstagram;

        // @NotEmpty(message = "Alasan Tidak Boleh Kosong")
        @Size(max = 255, message = "Jumlah Karakter Alasan maksimal 255 karakter ")
        @Column(name = "alasan", length = 255)
        private String alasan;

        // @NotEmpty(message = "Kelebihan/Kekurangan Tidak Boleh Kosong")
        @Size(max = 255, message = "Jumlah Karakter Kelebihan/Kekurangan maksimal 255 karakter ")
        @Column(name = "kelebihan_kerurangan", length = 255)
        private String kelebihanKekurangan;

        /**
         * 1. Kuliah/Sekolah
         * 2. kerja
         * 3. tidak keduanya
         */
        // @NotNull(message = "Kesibukan int Tidak Boleh Kosong")
        @JsonIgnoreProperties({"created_at","updated_at","deleted","description","flag", "subFlag"})
        @ManyToOne(targetEntity = Status.class,fetch = FetchType.EAGER)
        @JoinColumn(name = "kesibukan")
        private Status kesibukan;

        // @NotNull(message = "Data Status Tidak Boleh Kosong")
        // @JsonIgnoreProperties({"created_at","updated_at","deleted","description"})
        // @ManyToOne(targetEntity = Status.class, fetch = FetchType.EAGER)
        // @JoinColumn(name = "kegiatan", nullable = false)
        // private Status kegiatan;

        /**
         * mempunyai laptop
         */
        @Column(name = "laptop")
        private boolean laptop;

        /**
         * komitmen untuk mengikuti program
         */
        @Column(name = "komitmen")
        private boolean komitmen;

        /**
         * komitmen untuk mengikuti program
         */
        @Column(name = "siap_bekerja")
        private boolean siapBekerja;

        // peserta
        @JsonIgnoreProperties({"description", "banned","banned_time", "created_by","created_time","updated_time"})
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "class_id", nullable = true)
        private Kelas kelas;

        // @ManyToOne(fetch = FetchType.EAGER)
        // @JoinColumn(name = "education_id",nullable = true, updatable = false)
        // private Education education;
        /**
         * score test awal dan akhir
         */
        @Column(name = "score_test_awal")
        private Integer scoreTestAwal;

        @Column(name = "score_test_akhir")
        private Integer scoreTestAkhir;

        @Column(name = "upload_cv")
        @ApiModelProperty(hidden = true)
        private String uploadCv;

        @Column(name = "upload_cv_path")
        @ApiModelProperty(hidden = true)
        private String uploadCvPath;

        @Column(name = "nama_project")
        private String namaProject;

        // @Column(name = "jurusan")
        // private String jurusan;

        public Peserta() {
        }

        public Peserta(String namaPeserta, Date tanggalLahir, String jenisKelamin, String pendidikanTerakhir,
                        String noHp, String email, String alamatRumah, String motivasi, String kodeReferal,
                        String nomorKtp) {

                Date date = new Date();

                this.namaPeserta = namaPeserta;
                this.tanggalLahir = tanggalLahir;
                this.jenisKelamin = jenisKelamin;
                //this.pendidikanTerakhir = pendidikanTerakhir;
                this.noHp = noHp;
                this.email = email;
                this.alamatRumah = alamatRumah;
                this.motivasi = motivasi;
                this.kodeReferal = kodeReferal;
                this.created_time = date;
                this.updated_time = date;
                this.nomorKtp = nomorKtp;
        }

        // public Peserta(String namaPeserta, Date tanggalLahir, String jenisKelamin,
        // String pendidikanTerakhir,
        // String noHp, String email,String alamatRumah, String motivasi, String
        // kodeReferal, String nomorKtp) {
        //
        // Date date = new Date();
        //
        // this.namaPeserta = namaPeserta;
        // this.tanggalLahir = tanggalLahir;
        // this.jenisKelamin = jenisKelamin;
        // this.pendidikanTerakhir = pendidikanTerakhir;
        // this.noHp = noHp;
        // this.email = email;
        // this.alamatRumah = alamatRumah;
        // this.motivasi = motivasi;
        // this.kodeReferal = kodeReferal;
        // this.created_time = date;
        // this.updated_time = date;
        // this.nomorKtp = nomorKtp;
        //
        // }
}
