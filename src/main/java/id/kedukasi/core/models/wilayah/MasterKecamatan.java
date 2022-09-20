package id.kedukasi.core.models.wilayah;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "master_kecamatan")
public class MasterKecamatan implements Serializable {

    @Id
    private Integer id;

    @NotBlank
    @Column(name = "kota_id")
    private Integer kota_id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "alt_name")
    private String alt_name;

    @NotBlank
    @Column(name = "latitude")
    private Float latitude;

    @NotBlank
    @Column(name = "longitude")
    private Float longitude;

    public MasterKecamatan() {
    }

    public MasterKecamatan(Integer id, Integer kota_id, String name, String alt_name, Float latitude, Float longitude) {
        this.id = id;
        this.kota_id = kota_id;
        this.name = name;
        this.alt_name = alt_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
