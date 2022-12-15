package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

public class StatusRequest {

    @ApiModelProperty(example = "PESERTA/CALON PESERTA/REGISTER", required = true)
    private String statusName;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    @ApiModelProperty(example = "MENTOR/PESERTA", required = true)
    private String flag;

    @ApiModelProperty(example = "true", required = true)
    private boolean isDeleted;

    public StatusRequest() {
    }

    public StatusRequest(String status_name, String description, String flag, boolean isDeleted) {
        this.statusName = status_name;
        this.description = description;
        this.flag = flag;
        this.isDeleted = isDeleted;
    }

    public String getStatus_name() {
        return statusName;
    }

    public void setStatus_name(String status_name) {
        this.statusName = status_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean getisDeleted() {
        return isDeleted;
    }

    public void setisDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
