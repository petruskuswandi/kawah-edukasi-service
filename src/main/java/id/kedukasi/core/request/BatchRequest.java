package id.kedukasi.core.request;

import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Mentor;

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
    private String description;

    @NotBlank
    private Date startedtime;

    @NotBlank
    private Date endedtime;

    @NotBlank
    private Long classname;


    private Long mentorname;

    public Long getClassname() {
        return classname;
    }

    public void setClassname(Long classname) {
        this.classname = classname;
    }

    public Long getMentorname() {
        return mentorname;
    }

    public void setMentorname(Long mentorname) {
        this.mentorname = mentorname;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
