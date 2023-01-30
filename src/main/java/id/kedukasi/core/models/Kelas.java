package id.kedukasi.core.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "classes")
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = "classname"),
//        })
@DynamicUpdate


public class Kelas {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native")
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
    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    private Date banned_time;

    @Column(name = "created_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    private Date created_time;

//    @Column(name = "created_by", updatable = false)

    @JsonIgnoreProperties({"profilePicture","profilePicturePath","email","password","namaLengkap","noHp","role","isLogin","isActive","tokenVerification","created_time","updated_time","banned","banned_time","verified"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", nullable = false)
    @NotNull(message = "class id tidak boleh kosong")
    private User created_by;

    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    private Date updated_time;

    public Kelas() {
    }

    public Kelas(Long id, String classname, String description, boolean banned, Date banned_time, Date created_time, User created_by, Date updated_time) {
        Date date = new Date();
        this.id = id;
        this.classname = classname;
        this.description = description;
        this.banned = banned;
        this.banned_time = banned_time;
        this.created_time = created_time;
        this.created_by = created_by;
        this.updated_time = updated_time;
    }

    //    contruktor untuk update
    public Kelas(Long id, String className, String description) {
        Date date = new Date();
        this.id = id;
        this.classname = className;
        this.description = description;
        this.updated_time = date;

    }
//    construktor untuk create
    public Kelas(String className, String description) {
        Date date = new Date();
        this.classname = className;
        this.description = description;
        this.created_time = date;
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

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public Date getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Date updated_time) {
        this.updated_time = updated_time;
    }
}
