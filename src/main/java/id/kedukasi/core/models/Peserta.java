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
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "peserta",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "namaPeserta"),
                @UniqueConstraint(columnNames = "email")
        })
@DynamicUpdate
public class Peserta implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinTable(name = "peserta_kelas",
                joinColumns = @JoinColumn(name = "peserta_id"),
                inverseJoinColumns = @JoinColumn(name = "kelas_id"))
        private Kelas kelas;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinTable(name = "peserta_batch",
                joinColumns = @JoinColumn(name = "peserta_id"),
                inverseJoinColumns = @JoinColumn(name = "batch_id"))
        private Batch batch;

        @NotBlank
        @Column(name = "namaPeserta")
        private String namaPeserta;
        @Column(name = "nomorKtp")
        private String nomorKtp;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "tanggal_lahir")
        private Date tanggalLahir;

        private String jenisKelamin;

        private String pendidikanTerakhir;

        @NotBlank
        private String noHp;

        @NotBlank
        @Size(max = 50)
        @Email
        private String email;

        @Lob
        @Column(name = "uploadImage")
        @ApiModelProperty(hidden = true)
        private byte[] uploadImage;

        @Lob
        @Column(name = "uploadCv")
        @ApiModelProperty(hidden = true)
        private byte[] uploadCv;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinTable(name = "peserta_provinsi",
                joinColumns = @JoinColumn(name = "peserta_id"),
                inverseJoinColumns = @JoinColumn(name = "provinsi_id"))
        private MasterProvinsi provinsi;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinTable(name = "peserta_kota",
                joinColumns = @JoinColumn(name = "peserta_id"),
                inverseJoinColumns = @JoinColumn(name = "kota_id"))
        private MasterKota kota;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinTable(name = "peserta_kecamatan",
                joinColumns = @JoinColumn(name = "peserta_id"),
                inverseJoinColumns = @JoinColumn(name = "kecamatan_id"))
        private MasterKecamatan kecamatan;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinTable(name = "peserta_kelurahan",
                joinColumns = @JoinColumn(name = "peserta_id"),
                inverseJoinColumns = @JoinColumn(name = "kelurahan_id"))
        private MasterKelurahan kelurahan;

        private String alamatRumah;

        private String motivasi;

        @Column(name = "kodeReferal")
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

        @Column(name = "created_time", updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        private Date created_time;

        @Column(name = "updated_time")
        @Temporal(TemporalType.TIMESTAMP)
        private Date updated_time;

        public Peserta() {
        }

        public Peserta(String namaPeserta, Date tanggalLahir, String jenisKelamin, String pendidikanTerakhir,
                       String noHp, String email,String alamatRumah, String motivasi, String kodeReferal, String nomorKtp) {

                Date date = new Date();

                this.namaPeserta = namaPeserta;
                this.tanggalLahir = tanggalLahir;
                this.jenisKelamin = jenisKelamin;
                this.pendidikanTerakhir = pendidikanTerakhir;
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
