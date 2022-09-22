package id.kedukasi.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "classes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "classname"),
        })
@DynamicUpdate
public class Kelas implements Serializable {

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
    private Date banned_time;

    @Column(name = "created_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_time;

    @Column(name = "created_by", updatable = false)
    private String created_by;

    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_time;

    public Kelas() {
    }

    public Kelas(String classname, String description, String created_by) {
        Date date = new Date();
        this.classname = classname;
        this.description = description;
        this.created_by = created_by;
        this.created_time = date;
        this.updated_time = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public Date getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Date updated_time) {
        this.updated_time = updated_time;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public Date getBanned_time() {
        return banned_time;
    }

    public void setBanned_time(Date banned_time) {
        this.banned_time = banned_time;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}
