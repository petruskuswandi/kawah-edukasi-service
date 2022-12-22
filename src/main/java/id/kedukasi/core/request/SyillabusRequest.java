package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;


public class SyillabusRequest {

    @ApiModelProperty(example = "nama syllabus", required = true)
    private String syillabusName;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    @ApiModelProperty(example = "1", required = true)
    private Long attachment;

    public SyillabusRequest() {
    }

    public SyillabusRequest(String syillabusName, String description) {
        this.syillabusName = syillabusName;
        this.description = description;
    }

    public String getSyillabusName() {
        return syillabusName;
    }

    public void setSyillabusName(String syillabusName) {
        this.syillabusName = syillabusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAttachment() {
        return attachment;
    }

    public void setAttachment(Long attachment) {
        this.attachment = attachment;
    }
}
