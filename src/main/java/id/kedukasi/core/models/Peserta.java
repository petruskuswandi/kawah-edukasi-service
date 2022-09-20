package id.kedukasi.core.models;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.enums.EnumStatusTes;
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
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinTable(name = "peserta_kelas",
                joinColumns = @JoinColumn(name = "peserta_id"),
                inverseJoinColumns = @JoinColumn(name = "kelas_id"))
        private Kelas kelas;

//        Long Batch;

        @NotBlank
        @Column(name = "namaPeserta")
        private String namaPeserta;

        @Temporal(TemporalType.TIMESTAMP)
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
        @Column(name = "uploadImage", updatable = false)
        @ApiModelProperty(hidden = true)
        private byte[] uploadImage;

        @Column(name = "uploadImagePath", updatable = false)
        @ApiModelProperty(hidden = true)
        private String uploadImagePath;

        private String provinsi;

        private String kota;

        private String kecamatan;

        private String kelurahan;

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

        @Column(name = "banned", updatable = false)
        private boolean banned;

        @Column(name = "banned_time", updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        private Date banned_time;

        @Column(name = "updated_time")
        @Temporal(TemporalType.TIMESTAMP)
        private Date updated_time;

        public Peserta() {
        }

        public Peserta(String namaPeserta, Date tanggalLahir, String jenisKelamin, String pendidikanTerakhir,
                       String noHp, String email, String provinsi, String kota, String kecamatan, String kelurahan,
                       String alamatRumah, String motivasi, String kodeReferal) {

                Date date = new Date();

                this.namaPeserta = namaPeserta;
                this.tanggalLahir = tanggalLahir;
                this.jenisKelamin = jenisKelamin;
                this.pendidikanTerakhir = pendidikanTerakhir;
                this.noHp = noHp;
                this.email = email;
                this.provinsi = provinsi;
                this.kota = kota;
                this.kecamatan = kecamatan;
                this.kelurahan = kelurahan;
                this.alamatRumah = alamatRumah;
                this.motivasi = motivasi;
                this.kodeReferal = kodeReferal;
                this.updated_time = date;
        }
}
