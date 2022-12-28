package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;



public class KelasRequest {

    @ApiModelProperty(example = "nama_kelas", required = true)
    private String className;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    public KelasRequest() {
    }

    public KelasRequest(String className, String description) {
        this.className = className;
        this.description = description;
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
}
