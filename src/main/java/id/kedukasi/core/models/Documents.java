package id.kedukasi.core.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "documents")
@DynamicUpdate
public class Documents implements Serializable{
    /**
     * end point petrus dan kiki dana
     * kikidana
     * - POST
     * - GET ALL
     * - GET ID
     * petrus
     * - PUT
     * - DELETE
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_doc_id", nullable = false, updatable = false)
    @NotNull(message = "Data Batch Tidak Boleh Kosong")
    private TypeDocuments typedoc;

    @Column(name="url", nullable = false, length = 50)
    private String url;

    @Column(name="directory", nullable = false, length = 50)
    private String directory;

    @Column(name="key", nullable = false)
    private String key;

    @Column(name="filetype", nullable = false, length = 5)
    private String filetype;

    @Column(name="file_name", nullable = false, length = 50)
    private String documentsName;

    @Column(name="isDeleted")
    private boolean isDeleted;

    @Column(name="user_id")
    private int userId;

    @Column(name="role_id")
    private int roleId;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date created_at;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updated_at;

    public Documents() {

    }

    public Documents(int id, String url, String directory, String key, String filetype, String documentsName,
                     boolean isDeleted, int userId, int roleId, Date created_at, Date updated_at) {
        this.id = id;
        this.url = url;
        this.directory = directory;
        this.key = key;
        this.filetype = filetype;
        this.documentsName = documentsName;
        this.isDeleted = isDeleted;
        this.userId = userId;
        this.roleId = roleId;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Documents(int id, String url, String directory, String key, String filetype, String documentsName,
                     boolean isDeleted, int userId, int roleId) {
        this.id = id;
        this.url = url;
        this.directory = directory;
        this.key = key;
        this.filetype = filetype;
        this.documentsName = documentsName;
        this.isDeleted = isDeleted;
        this.userId = userId;
        this.roleId = roleId;
    }

    public Documents(String url, String directory, String key, String filetype, String documentsName, boolean isDeleted,
                     int userId, int roleId) {
        super();
        this.url = url;
        this.directory = directory;
        this.key = key;
        this.filetype = filetype;
        this.documentsName = documentsName;
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

    public void setDocumentsName(String documentsName) {
        this.documentsName = documentsName;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
