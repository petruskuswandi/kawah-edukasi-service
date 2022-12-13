package id.kedukasi.core.models;

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
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "peserta",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "no_hp"),
                @UniqueConstraint(columnNames = "nomor_ktp"),
        })
@DynamicUpdate
public class Peserta implements Serializable {


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "batch_id", nullable = false, updatable = false)
        @NotNull(message = "Data Batch Tidak Boleh Kosong")
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

        //register
        @NotEmpty(message = "Nama Tidak Boleh Kosong")
        @Column(name = "nama_peserta", nullable = false)
        @Size(max = 50, message = "Jumlah Karakter Nama Peserta maksimal 50 karakter ")
        private String namaPeserta;

        @Size(max = 20, message = "Jumlah Karakter No KTP Peserta maksimal 20 karakter ")
        @Column(name = "nomor_ktp")
        private String nomorKtp;

        //register
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

        //register
        @NotEmpty(message = "Np HP Tidak Boleh Kosong")
        @Pattern(regexp = "^(\\+62|62)?[\\s-]?0?8[1-9]{1}\\d{1}[\\s-]?\\d{4}[\\s-]?\\d{2,5}$", message = "Format Hp Tidak Sesuai")
        @Column(name = "no_hp", length = 15)
        private String noHp;
        //register
        @NotEmpty(message = "Email Tidak Boleh Kosong")
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

        //register
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "provinsi_id", nullable = false, updatable = false)
        @NotNull(message = "Data Provinsi Tidak Boleh Kosong")
        private MasterProvinsi provinsi;

        //register
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "kota_id", nullable = false, updatable = false)
        @NotNull(message = "Data Kota Tidak Boleh Kosong")
        private MasterKota kota;

        //register
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "kecamatan_id", nullable = false, updatable = false)
        @NotNull(message = "Data Kecamatan Tidak Boleh Kosong")
        private MasterKecamatan kecamatan;

        //register
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "kelurahan_id", nullable = false, updatable = false)
        @NotNull(message = "Data Kelurahan Tidak Boleh Kosong")
        private MasterKelurahan kelurahan;

        //register
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "status_id", nullable = false, updatable = false)
        @NotNull(message = "Data Status Tidak Boleh Kosong")
        private Status status;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "education_id", nullable = true, updatable = false)
        private Education pendidikanTerakhir;




        //register
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
        @Column(name = "statusTes",length = 20)
        private EnumStatusTes statusTes;

        @Enumerated(EnumType.STRING)
        @Column(name = "statusPeserta",length = 20)
        private EnumStatusPeserta statusPeserta;

        @Column(name = "banned")
        private boolean banned;

        @Column(name = "banned_time")
        @Temporal(TemporalType.TIMESTAMP)
        private Date banned_time;

        @CreationTimestamp
        @Column(name = "created_time", updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        private Date created_time;

        @UpdateTimestamp
        @Column(name = "updated_time")
        @Temporal(TemporalType.TIMESTAMP)
        private Date updated_time;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "program_id", nullable = false, updatable = false)
        @NotNull(message = "Data Program Tidak Boleh Kosong")
        private Program program;

        /**
         *  status tag twitbbon
         */
        @AssertTrue(message = "status twittbon harus selalu true")
        @Column(name = "stat_twibbon", nullable = false)
        private boolean statTwibbon;

        @NotEmpty(message = "Link Untuk Twittbon Tidak Boleh Kosong")
        @Column(name = "link_twittbon", length = 50, nullable = false)
        @Size(max = 50, message = "Jumlah Karakter Link Twittbon maksimal 50 karakter ")
        private String linkTwiitbon;
        /**
         * 1. java
         * 2. tidak menguasai
         * 3. other
         */
        @NotNull(message = "pemrograman int Tidak Boleh Kosong")
        @Column(name = "pemrograman", length = 3, nullable = false)
        private Integer pemrograman;

        @Column(name = "keterangan_pemrograman",length = 255)
        @Size(max = 255, message = "Jumlah Karakter Keterangan Pemrograman maksimal 255 karakter ")
        private String keteranganPemrograman;

        /**
         * mengikuti bootcamp sebelumnya
         */
        @Column(name = "stat_bootcamp",nullable = false)
        private boolean statBootcamp;

        @Column(name = "nama_bootcamp",length = 50)
        @Size(max = 50, message = "Jumlah Karakter nama bootcamp maksimal 50 karakter ")
        private String namaBootcamp;

        @NotEmpty(message = "Sekolah/Universitas Tidak Boleh Kosong")
        @Column(name = "sekolah_universitas",length = 50, nullable = false)
        @Size(max = 50, message = "Jumlah Karakter Sekolah/Universitas maksimal 50 karakter ")
        private String sekolahUniversitas;

        @NotEmpty(message = "Jurusan Tidak Boleh Kosong")
        @Size(max = 50, message = "Jumlah Karakter Jurusan maksimal 50 karakter ")
        @Column(name = "jurusan",length = 50, nullable = false)
        private String jurusan;

        @NotEmpty(message = "Tahun Lulus Tidak Boleh Kosong")
        @Size(max = 4, message = "Jumlah Karakter Tahun Lulus maksimal 4 karakter ")
        @Column(name = "tahun_lulus",length = 4, nullable = false)
        private String tahunLulus;

        @NotEmpty(message = "User Instagram Tidak Boleh Kosong")
        @Size(max = 50, message = "Jumlah Karakter User Instagram maksimal 50 karakter ")
        @Column(name = "user_instagram",length = 50, nullable = false)
        private String userInstagram;

        @NotEmpty(message = "Alasan Tidak Boleh Kosong")
        @Size(max = 255, message = "Jumlah Karakter Alasan maksimal 255 karakter ")
        @Column(name = "alasan", length = 255, nullable = false)
        private String alasan;

        @NotEmpty(message = "Kelebihan/Kekurangan Tidak Boleh Kosong")
        @Size(max = 255, message = "Jumlah Karakter Kelebihan/Kekurangan maksimal 255 karakter ")
        @Column(name = "kelebihan_kerurangan", length = 255, nullable = false)
        private String kelebihanKekurangan;


        /**
         * 1. Kuliah/Sekolah
         * 2. kerja
         * 3. tidak keduanya
         */
        @NotNull(message = "Kesibukan int Tidak Boleh Kosong")
        @Column(name = "kesibukan", nullable = false)
        private Integer kesibukan;

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

        public Peserta() {
        }

        public Peserta(String namaPeserta, Date tanggalLahir, String jenisKelamin, String pendidikanTerakhir,
                       String noHp, String email,String alamatRumah, String motivasi, String kodeReferal, String nomorKtp) {

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
//        public Peserta(String namaPeserta, Date tanggalLahir, String jenisKelamin, String pendidikanTerakhir,
//                       String noHp, String email,String alamatRumah, String motivasi, String kodeReferal, String nomorKtp) {
//
//                Date date = new Date();
//
//                this.namaPeserta = namaPeserta;
//                this.tanggalLahir = tanggalLahir;
//                this.jenisKelamin = jenisKelamin;
//                this.pendidikanTerakhir = pendidikanTerakhir;
//                this.noHp = noHp;
//                this.email = email;
//                this.alamatRumah = alamatRumah;
//                this.motivasi = motivasi;
//                this.kodeReferal = kodeReferal;
//                this.created_time = date;
//                this.updated_time = date;
//                this.nomorKtp = nomorKtp;
//
//        }
}
