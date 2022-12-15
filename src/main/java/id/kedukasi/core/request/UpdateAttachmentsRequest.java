package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

public class UpdateAttachmentsRequest {

    @ApiModelProperty(example = "1", required = true)
    private Long id;

    @ApiModelProperty(example = "url", required = true)
    private String url;


    @ApiModelProperty(example = "directory", required = true)
    private String directory;

    @ApiModelProperty(example = "key", required = true)
    private String key;

    @ApiModelProperty(example = "filetype", required = true)
    private String filetype;

    @ApiModelProperty(example = "filename", required = true)
    private String filename;

    @ApiModelProperty(example = "true", required = true)
    private boolean isDeleted;

    public UpdateAttachmentsRequest() {
    }
    public UpdateAttachmentsRequest(Long id, String url, String directory, String key, String filetype,
                                    String filename, boolean isDeleted) {
        this.id = id;
        this.url = url;
        this.directory = directory;
        this.key = key;
        this.filetype = filetype;
        this.filename = filename;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFile_name() {
        return filename;
    }

    public void setFile_name(String filename) {
        this.filename = filename;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
