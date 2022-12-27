package id.kedukasi.core.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Data
@Entity
@Table(name = "classes")
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = "classname"),
//        })
@DynamicUpdate
public class Kelas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String classname;

    @NotBlank
    @Size(max = 50)
    private String description;

    @Column(name = "banned")
    private boolean banned;

    @Column(name = "banned_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date banned_time;

    @Column(name = "created_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date created_time;

    @Column(name = "created_by_id", updatable = false)
    private String created_id;

    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date updated_time;

    public Kelas() {
    }


    public Kelas(String className, String description, Long nameId) {
    }
}
