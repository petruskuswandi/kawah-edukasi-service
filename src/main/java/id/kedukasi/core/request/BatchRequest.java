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
    private String description;

    @NotBlank
    private Date startedtime;

    @NotBlank
    private Date endedtime;

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
