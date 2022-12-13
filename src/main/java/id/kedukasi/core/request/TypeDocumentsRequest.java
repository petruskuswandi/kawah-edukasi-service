package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

public class TypeDocumentsRequest {
    @ApiModelProperty(example = "nama",required = true)
    private String typeName;

    @ApiModelProperty(example = "deskripsi",required = true)
    private String description;


    public TypeDocumentsRequest() {
    }

    public TypeDocumentsRequest(String typeName, String description) {
        this.typeName = typeName;
        this.description = description;

    }

    public String getType_Name() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}