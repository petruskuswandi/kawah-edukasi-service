package id.kedukasi.core.models.wilayah;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "master_provinsi")
public class MasterProvinsi implements Serializable {

    @Id
    private Integer id;

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

    public MasterProvinsi() {
    }

    public MasterProvinsi(Integer id, String name, String alt_name, Float latitude, Float longitude) {
        this.id = id;
        this.name = name;
        this.alt_name = alt_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
