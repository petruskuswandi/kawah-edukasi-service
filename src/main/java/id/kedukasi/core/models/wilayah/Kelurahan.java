package id.kedukasi.core.models.wilayah;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "master_kelurahan")

public class Kelurahan implements Serializable{
    
    @Id
    private Integer id;

    @NotBlank
    @Column(name = "kecamatan_id")
    private Integer kecamatan_id;

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

    public Kelurahan() {
    }

    public Kelurahan(Integer kecamatan_id, String name, String alt_name, Double latitude, Double longitude) {
        this.kecamatan_id = kecamatan_id;
        this.name = name;
        this.alt_name = alt_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
