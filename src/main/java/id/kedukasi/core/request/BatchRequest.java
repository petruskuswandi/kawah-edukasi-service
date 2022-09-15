package id.kedukasi.core.request;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import java.util.Date;


public class BatchRequest {
    private  Long id;

    @NotBlank
    private String batchname;

    @NotBlank
    private String alamarrumahmentor;

    @NotBlank
    private Integer mentorid;

    @NotBlank
    private Integer classid;

    @Column(name = "started_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date starDate;

    @Column(name = "ended_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

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

    public String getAlamarrumahmentor() {
        return alamarrumahmentor;
    }

    public void setAlamarrumahmentor(String alamarrumahmentor) {
        this.alamarrumahmentor = alamarrumahmentor;
    }

    public Integer getMentorid() {
        return mentorid;
    }

    public void setMentorid(Integer mentorid) {
        this.mentorid = mentorid;
    }

    public Integer getClassid() {
        return classid;
    }

    public void setClassid(Integer classid) {
        this.classid = classid;
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
