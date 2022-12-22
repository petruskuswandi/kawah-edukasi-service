package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

public class UpdateSyllabusRequest {
    
    @ApiModelProperty(example = "1", required = true)
    private Long id;

    @ApiModelProperty(example = "nama syllabus", required = true)
    private String syllabusName;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    @ApiModelProperty(example = "true", required = true)
    private boolean softDelete;

    @ApiModelProperty(example = "1", required = true)
    private Long attachment;


    public UpdateSyllabusRequest() {
    }

    public UpdateSyllabusRequest(Long id, String syllabusName, String description, boolean softDelete) {
        this.id = id;
        this.syllabusName = syllabusName;
        this.description = description;
        this.softDelete = softDelete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSyllabusName() {
        return syllabusName;
    }

    public void setSyllabusName(String syllabusName) {
        this.syllabusName = syllabusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(boolean softDelete) {
        this.softDelete = softDelete;
    }

    public Long getAttachment() {
        return attachment;
    }

    public void setAttachment(Long attachment) {
        this.attachment = attachment;
    }
}
