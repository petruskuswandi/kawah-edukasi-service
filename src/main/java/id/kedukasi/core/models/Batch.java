package id.kedukasi.core.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

@Getter
@Setter
@Entity
@Table(name = "batches")

@DynamicUpdate
public class Batch implements Serializable {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native")
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String batchname;

    @NotBlank
    @Size(max = 100)
    private String description;

    @Column(name = "banned")
    private boolean banned;

    @Column(name = "banned_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    private Date banned_time;

    // menambahkan stadate & Endedate
    @Column(name = "started_time")
    @Temporal(DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    private Date startedtime;

    @Column(name = "ended_time")
    @Temporal(DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    private Date endedtime;
    @JsonIgnoreProperties({"profilePicture","profilePicturePath","email","password","namaLengkap","noHp","role","isLogin","isActive","tokenVerification","created_time","updated_time","banned","banned_time","verified"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User created_by;

    @Column(name = "created_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    private Date created_time;

    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    private Date updated_time;

    public Batch() {
    }


    public Batch(String batchname, String description,Date startedtime, Date endedtime, User created_by) {
        Date date = new Date();

        this.batchname = batchname;
        this.description = description;
        this.startedtime = startedtime;
        this.endedtime = endedtime;
        this.created_time = date;
        this.updated_time = date;
        this.created_by = created_by;
        this.banned = false;
        this.banned_time = date;
    }

    //constructur untuk create
    public Batch(String batchname, String description, Date startedtime, Date endedtime) {
        this.batchname = batchname;
        this.description = description;
        this.startedtime = startedtime;
        this.endedtime = endedtime;
    }

    public Batch(Long id, String batchname, Date startedtime, Date endedtime) {
        this.id = id;
        this.batchname = batchname;
        this.startedtime = startedtime;
        this.endedtime = endedtime;
    }
}
