package id.kedukasi.core.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "program")
@DynamicUpdate
public class Program implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 30)
    @Column(name="program_name", nullable = false)
    private String programName;


    @Size(max = 100)
    private String description;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date created_at;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updated_at;

    @Column(name = "soft_delete", nullable = false)
    private boolean soft_delete;

    public Program() {
    }

    public Program(Long id, String programName, String description, Date created_at, Date updated_at, boolean soft_delete) {
        this.id = id;
        this.programName = programName;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.soft_delete = soft_delete;
    }

    public Program(Long id, String programName, String description, boolean soft_delete) {
        this.id = id;
        this.programName = programName;
        this.description = description;
        this.soft_delete = soft_delete;
    }

    public Program(String programName, String description, boolean soft_delete) {
        this.programName = programName;
        this.description = description;
        this.soft_delete = soft_delete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isSoft_delete() {
        return soft_delete;
    }

    public void setSoft_delete(boolean soft_delete) {
        this.soft_delete = soft_delete;
    }
}
