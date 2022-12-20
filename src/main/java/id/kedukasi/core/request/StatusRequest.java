package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

public class StatusRequest {

    @ApiModelProperty(example = "REGISTER", required = true)
    private String statusName;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    @ApiModelProperty(example = "PESERTA", required = true)
    private String flag;

    @ApiModelProperty(example = "Suspended", required = true)
    private String subFlag;

    @ApiModelProperty(example = "false", required = true)
    private boolean isDeleted;

    public StatusRequest() {
    }

    public StatusRequest(String status_name, String description, String flag, String subFlag, boolean isDeleted) {
        this.statusName = status_name;
        this.description = description;
        this.flag = flag;
        this.subFlag = subFlag;
        this.isDeleted = isDeleted;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String status_name) {
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

    public String getSubFlag() {
        return subFlag;
    }

    public void setSubFlag(String subFlag) {
        this.subFlag = subFlag;
    }

    public boolean getisDeleted() {
        return isDeleted;
    }

    public void setisDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
