package id.kedukasi.core.models.wilayah;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "master_kelurahan")
public class MasterKelurahan implements Serializable {

    @Id
    private Long id;

    @NotBlank
    @Column(name = "kecamatan_id")
    private Long kecamatan_id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "alt_name")
    private String alt_name;

    @NotBlank
    @Column(name = "latitude")
    private Double latitude;

    @NotBlank
    @Column(name = "longitude")
    private Double longitude;

    public MasterKelurahan() {
    }

    public MasterKelurahan(Long id, Long kecamatan_id, String name, String alt_name, Double latitude, Double longitude) {
        this.id = id;
        this.kecamatan_id = kecamatan_id;
        this.name = name;
        this.alt_name = alt_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
