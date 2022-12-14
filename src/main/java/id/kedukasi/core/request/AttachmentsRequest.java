package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

public class AttachmentsRequest {

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

    public AttachmentsRequest() {
    }

    public AttachmentsRequest(String url, String directory, String key, String filetype, String filename) {
        this.url = url;
        this.directory = directory;
        this.key = key;
        this.filetype = filetype;
        this.filename = filename;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
