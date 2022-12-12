package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

public class UpdateTypeRequest {


    @ApiModelProperty(example = "1",required = true)
    private int id;
    @ApiModelProperty (name = "type_name" ,example = "Nama type document",required = true)
    private String type_Name;
    @ApiModelProperty (example = "deskripsi",required = true)
    private String description;
    @ApiModelProperty (example = "false",required = true)
    private boolean isDeleted;

    public UpdateTypeRequest() {
    }

    public UpdateTypeRequest(int id, String type_Name, String description, boolean isDeleted) {
        this.id = id;
        this.type_Name = type_Name;
        this.description = description;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_Name() {
        return type_Name;
    }

    public void setType_Name(String type_Name) {
        this.type_Name = type_Name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
