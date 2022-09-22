package id.kedukasi.core.models;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "batches",uniqueConstraints = {
        @UniqueConstraint(columnNames = "batchname"),
})

@DynamicUpdate
public class Batch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String batchname;

    @NotBlank
    @Size(max = 100)
    private String alamatrumahmentor;


    // class dan mentor
//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "classes",
//            joinColumns = @JoinColumn(name = "classname"),
//            inverseJoinColumns = @JoinColumn(name = "classname"))
//    private String classname;
//
//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "mentores",
//            joinColumns = @JoinColumn(name = "mentorname"),
//            inverseJoinColumns = @JoinColumn(name = "mentorname"))
//    private String mentorname;

    @Column(name = "banned")
    private boolean banned;

    @Column(name = "banned_time", updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date banned_time;

    @Column(name = "created_time", updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_time;

    // menambahkan stadate & Endedate
    @Column(name = "started_time", updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedtime;

    @Column(name = "ended_time", updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endedtime;

    @Column(name = "created_by")
    private String created_by;

    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_time;

    public Batch() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchname() {
        return batchname;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    public String getAlamatrumahmentor() {
        return alamatrumahmentor;
    }

    public void setAlamatrumahmentor(String alamatrumahmentor) {
        this.alamatrumahmentor = alamatrumahmentor;
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

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Date getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Date updated_time) {
        this.updated_time = updated_time;
    }

    public Date getStartedtime() {
        return startedtime;
    }

    public void setStartedtime(Date startedtime) {
        this.startedtime = startedtime;
    }

    public Date getEndedtime() {
        return endedtime;
    }

    public void setEndedtime(Date endedtime) {
        this.endedtime = endedtime;
    }

    public Batch(String batchname, String alamatrumahmentor,Date startedtime, Date endedtime) {
        Date date = new Date();
        this.batchname = batchname;
        this.alamatrumahmentor = alamatrumahmentor;
        this.startedtime = startedtime;
        this.endedtime = endedtime;
        this.created_time = date;
        this.updated_time = date;
        this.banned = false;
        this.banned_time = date;
    }
}
