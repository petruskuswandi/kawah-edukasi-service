package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;
public class UpdateStatusRequest {
    @ApiModelProperty(example = "1", required = true)
    private int id;

    @ApiModelProperty(example = "REGISTER", required = true)
    private String statusName;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    @ApiModelProperty(example = "PESERTA", required = true)
    private String flag;

    @ApiModelProperty(example = "false", required = true)
    private boolean isDeleted;

    public UpdateStatusRequest() {
    }

    public UpdateStatusRequest(int id, String status_name, String description, String flag, boolean isDeleted) {
        this.id = id;
        this.statusName = status_name;
        this.description = description;
        this.flag = flag;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean getisDeleted() {
        return isDeleted;
    }

    public void setisDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
