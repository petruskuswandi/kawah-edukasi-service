package id.kedukasi.core.models.wilayah;

import com.fasterxml.jackson.annotation.JsonIgnore;
import id.kedukasi.core.models.Peserta;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "master_kota")
public class MasterKota implements Serializable {

    @Id
    private Long id;

    @NotBlank
    @Column(name = "province_id")
    private Long province_id;

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

    @JsonIgnore
    @OneToMany()
    private Set<Peserta> peserta;

    public MasterKota() {
    }

    public MasterKota(Long id, Long province_id, String name, String alt_name, Double latitude, Double longitude) {
        this.id = id;
        this.province_id = province_id;
        this.name = name;
        this.alt_name = alt_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
