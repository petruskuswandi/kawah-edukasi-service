package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;


public class UpdateKelasRequest {

    @ApiModelProperty(example = "1", required = true)
    private Long id;

    @ApiModelProperty(example = "nama_kelas", required = true)
    private String className;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    public UpdateKelasRequest() {
    }

    public UpdateKelasRequest(String className, String description) {
        this.id = id;
        this.className = className;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
