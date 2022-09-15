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

    @NotBlank
    private Integer classid;

    @NotBlank
    private Integer mentorid;


    @Column(name = "banned", updatable = false)
    private boolean banned;

    @Column(name = "banned_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date banned_time;

    @Column(name = "created_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_time;

    @Column(name = "created_by")
    private String created_by;

    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_time;

    @Column(name = "started_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date starDate;

    @Column(name = "ended_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public Batch() {

    }

    public Batch(String batchname, String alamatrumahmentor, Integer classid, Integer mentorid, Date starDate, Date endDate) {
        Date date = new Date();
        this.batchname = batchname;
        this.alamatrumahmentor = alamatrumahmentor;
        this.classid = classid;
        this.mentorid = mentorid;
        this.starDate = starDate;
        this.endDate = endDate;
        this.created_time = date;
        this.updated_time = date;
        this.banned = false;
        this.banned_time = date;
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

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public int getMentorid() {
        return mentorid;
    }

    public void setMentorid(int mentorid) {
        this.mentorid = mentorid;
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

    public Date getStarDate() {
        return starDate;
    }

    public void setStarDate(Date starDate) {
        this.starDate = starDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
