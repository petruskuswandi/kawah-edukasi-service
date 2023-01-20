package id.kedukasi.core.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "syillabus_detail")
@DynamicUpdate
public class SyillabusDetail {
    
    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native")
    private Long id;

    @JsonIgnoreProperties({"description", "banned","banned_time", "created_time","updated_time"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = false)
    @NotNull(message = "class id tidak boleh kosong")
    private Kelas kelas;

    @JsonIgnoreProperties({"description", "deleted","banned_time", "created_at","updated_at"})
    @ManyToMany(targetEntity = Syillabus.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @JoinColumn(name = "syillabus_id", nullable = false)
    @JoinTable(
        name = "syillabus_detail_syillabus",
        joinColumns = @JoinColumn(name = "syillabus_detail_id"),
        inverseJoinColumns = @JoinColumn(name = "syillabus_id")
    )
    @NotNull(message = "syillabus id tidak boleh kosong")
    // private Syillabus syillabus;
    public List<Syillabus> syillabus;

    @JsonIgnore
    @Column(name="is_deleted")
    private boolean isDeleted;

    @JsonIgnore
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date created_at;

    @JsonIgnore
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
