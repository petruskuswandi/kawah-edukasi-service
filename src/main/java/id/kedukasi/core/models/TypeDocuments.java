package id.kedukasi.core.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
@Entity
public class TypeDocuments {
    /**
     * riski kurniawan
     * - POST
     * - GET ALL
     * - GET BY ID
     * trian eka putra
     * - UPDATE
     * - DELETE
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="type_name", nullable = false, length = 10)
    private String typeName;

    @Column(name="description")
    private String description;

    @Column(name="isDeleted")
    private boolean isDeleted;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date created_at;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updated_at;


    public TypeDocuments() {
    }

    public TypeDocuments(int id, String typeName, String description, boolean isDeleted, Date created_at, Date updated_at) {
        this.id = id;
        this.typeName = typeName;
        this.description = description;
        this.isDeleted = isDeleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public TypeDocuments(int id, String typeName, String description, boolean isDeleted) {
        this.id = id;
        this.typeName = typeName;
        this.description = description;
        this.isDeleted = isDeleted;
    }

    public TypeDocuments(String typeName, String description, boolean isDeleted) {
        this.typeName = typeName;
        this.description = description;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
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
