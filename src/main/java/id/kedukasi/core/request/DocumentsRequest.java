package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

public class DocumentsRequest {

    @ApiModelProperty(example = "url file", required = true)
    private String url;

    @ApiModelProperty(example = "nama directory", required = true)
    private String directory;

    @ApiModelProperty(example = "key", required = true)
    private String key;

    @ApiModelProperty(example = "tipe file", required = true)
    private String filetype;

    @ApiModelProperty(example = "nama file", required = true)
    private String documentsName;

    @ApiModelProperty(example = "1", required = true)
    private int userId;

    @ApiModelProperty(example = "1", required = true)
    private int roleId;

    public DocumentsRequest() {

    }

    public DocumentsRequest(String url, String directory, String key, String filetype, String filename, int userId, int roleId) {
        this.url = url;
        this.directory = directory;
        this.key = key;
        this.documentsName = filename;
        this.filetype = filetype;
        this.userId = userId;
        this.roleId = roleId;
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

    public String getDocumentsName() {
        return documentsName;
    }

    public void setDocumentsName(String documentsName) {
        this.documentsName = documentsName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
