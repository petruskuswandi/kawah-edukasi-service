package id.kedukasi.core.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "syillabus_detail")
@DynamicUpdate
public class SyillabusDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = false)
    @NotNull(message = "class id tidak boleh kosong")
    private Kelas kelas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "syillabus_id", nullable = false)
    @NotNull(message = "syillabus id tidak boleh kosong")
    private Syillabus syillabus;

    @Column(name="is_deleted")
    private boolean isDeleted;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date created_at;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updated_at;

    public SyillabusDetail() {
    }

    public SyillabusDetail(Long id, boolean isDeleted) {
        Date date = new Date();
        
        this.id = id;
        this.isDeleted = isDeleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    
}
