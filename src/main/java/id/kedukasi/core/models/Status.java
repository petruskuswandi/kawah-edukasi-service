package id.kedukasi.core.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "status")
@DynamicUpdate
public class Status {
    /**
     * rizka tauria
     * - POST
     * - GET ALL
     * - GET BY ID
     * sirajuddin
     * - UPDATE
     * - DELETE
     */
    @Id
    @SequenceGenerator( 
        name = "statusSequence", 
        sequenceName = "status_id_seq", 
        allocationSize = 1, //Kenaikan nilai sebesar 1
        initialValue = 1) //Nilai awal sebesar 1
    @GeneratedValue( //Id status akan memiliki nilai sendiri yang tidak terpaut dengan nilai id entitas lain
        strategy = GenerationType.SEQUENCE, 
        generator = "statusSequence")
    private int id;

    /**
     * PESERTA
     * CALON PESERTA
     * REGISTER
     *
     *
     */
    @Column(name="status_name", nullable = false, length = 50)
    private String statusName;

    @Column(name="description", nullable = false)
    private String description;

    /**
     * - MENTOR
     * - PESERTA
     */
    @Column(name="flag", nullable = false) 
    private String flag;

    @Column(name="sub_flag", nullable = false) 
    private String subFlag;

    @Column(name="isDeleted")
    private boolean isDeleted;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    private Date created_at;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    private Date updated_at;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "role_id", nullable = true)
//    private Role role;

    public Status() {
    }

    public Status(int id, String statusName, String description, String flag, String subFlag, boolean isDeleted, Date created_at, Date updated_at) {
        this.id = id;
        this.statusName = statusName;
        this.description = description;
        this.flag = flag;
        this.subFlag = subFlag;
        this.isDeleted = isDeleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Status(int id, String statusName, String description, String flag, String subFlag, boolean isDeleted) {
        this.id = id;
        this.statusName = statusName;
        this.description = description;
        this.flag = flag;
        this.subFlag = subFlag;
        this.isDeleted = isDeleted;
    }

    public Status(String statusName, String description, String flag, String subFlag, boolean isDeleted) {
        this.statusName = statusName;
        this.description = description;
        this.flag = flag;
        this.subFlag = subFlag;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSubFlag() {
        return subFlag;
    }

    public void setSubFlag(String subFlag) {
        this.subFlag = subFlag;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setisDeleted(boolean isDeleted) {
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
}
