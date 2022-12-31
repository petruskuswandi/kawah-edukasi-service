package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;



public class KelasRequest {

    @ApiModelProperty(example = "nama_kelas", required = true)
    private String className;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    @ApiModelProperty(example = "1", required = true)
    private Integer created_by;

    public KelasRequest() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Integer created_by) {
        this.created_by = created_by;
    }
}
