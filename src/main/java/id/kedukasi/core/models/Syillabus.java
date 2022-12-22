package id.kedukasi.core.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name="syillabus")
@DynamicUpdate
public class Syillabus {
    /**
     * Muhammad Hilman
     * - POST
     * - GET ALL
     * - GET BY ID
     * raihan ramaahdan
     * - DELETE
     * - UPDATE
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="syllabus_name", nullable = false, length = 50)
    private String syillabusName;

    @Column(name="description", nullable = false, length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attachments_id", nullable = false)
    @NotNull(message = "Data Attachments Tidak Boleh Kosong")
    private Attachments attachments;

    @Column(name="isDeleted")
    private boolean isDeleted;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date created_at;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updated_at;

    public Syillabus() {
    }

    public Syillabus(Long id, String syillabusName, String description, boolean isDeleted, Date created_at,
            Date updated_at) {
        this.id = id;
        this.syillabusName = syillabusName;
        this.description = description;
        this.isDeleted = isDeleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Syillabus(Long id, String syillabusName, String description, boolean isDeleted) {
        this.id = id;
        this.syillabusName = syillabusName;
        this.description = description;
        this.isDeleted = isDeleted;
    }

    public Syillabus(String syillabusName, String description, boolean isDeleted) {
        this.syillabusName = syillabusName;
        this.description = description;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public Attachments getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachments attachments) {
        this.attachments = attachments;
    }
}

