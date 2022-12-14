package id.kedukasi.core.models;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "attachments")
@DynamicUpdate
public class Attachments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="url", nullable = false, length = 50)
    @NotBlank(message = "Url Tidak Boleh Kosong")
    private String url;

    @Column(name="directory", nullable = false, length = 50)
    @NotBlank(message = "Directory Tidak Boleh Kosong")
    private String directory;

    @Column(name="key", nullable = false)
    @NotBlank(message = "Key Tidak Boleh Kosong")
    private String key;

    @Column(name="filetype", nullable = false, length = 5)
    @NotBlank(message = "Filetype Tidak Boleh Kosong")
    private String filetype;

    @Column(name="filename", nullable = false, length = 50)
    @NotBlank(message = "Filename Tidak Boleh Kosong")
    private String attachmentsName;

    @Column(name="isDeleted")
    private boolean isDeleted;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date created_at;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updated_at;

    public Attachments() {
    }

    public Attachments(Long id, String url, String directory, String key, String filetype, String filename,
                       boolean isDeleted, Date created_at, Date updated_at) {
        this.id = id;
        this.url = url;
        this.directory = directory;
        this.key = key;
        this.filetype = filetype;
        this.attachmentsName = filename;
        this.isDeleted = isDeleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    public Attachments(Long id, String url, String directory, String key, String filetype, String filename,
                       boolean isDeleted) {
        this.id = id;
        this.url = url;
        this.directory = directory;
        this.key = key;
        this.filetype = filetype;
        this.attachmentsName = filename;
        this.isDeleted = isDeleted;
    }

    public Attachments(String url, String directory, String key, String filetype, String filename,
                       boolean isDeleted) {
        super();
        this.url = url;
        this.directory = directory;
        this.key = key;
        this.filetype = filetype;
        this.attachmentsName = filename;
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

    public String getFilename() {
        return attachmentsName;
    }

    public void setFilename(String filename) {
        this.attachmentsName = filename;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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
