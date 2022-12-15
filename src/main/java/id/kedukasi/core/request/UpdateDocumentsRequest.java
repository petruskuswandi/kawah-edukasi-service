package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

public class UpdateDocumentsRequest {

    @ApiModelProperty(example = "1", required = true)
    private int id;

    @ApiModelProperty(example = "url", required = true)
    private String url;

    @ApiModelProperty(example = "directory", required = true)
    private String directory;

    @ApiModelProperty(example = "key", required = true)
    private String key;

    @ApiModelProperty(example = "filetype", required = true)
    private String filetype;

    @ApiModelProperty(example = "file_name", required = true)
    private String documentsName;

    @ApiModelProperty(example = "true", required = true)
    private boolean isDeleted;

    @ApiModelProperty(example = "1", required = true)
    private int userId;

    @ApiModelProperty(example = "1", required = true)
    private int roleId;

    public UpdateDocumentsRequest() {
    }

    public UpdateDocumentsRequest(int id, String url, String directory, String key, String filetype,
                                  String file_name, boolean isDeleted, int userId, int roleId) {
        this.id = id;
        this.url = url;
        this.directory = directory;
        this.key = key;
        this.filetype = filetype;
        this.documentsName = file_name;
        this.isDeleted = isDeleted;
        this.userId = userId;
        this.roleId = roleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getDocumentsName() {
        return documentsName;
    }

    public void setDocumentsName(String file_name) {
        this.documentsName = file_name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
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
