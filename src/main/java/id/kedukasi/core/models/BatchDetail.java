package id.kedukasi.core.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "batch_detail")
@DynamicUpdate
public class BatchDetail{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relation
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batch_id", nullable = false)
    @NotNull(message = "Data Batch Tidak Boleh Kosong")
    public Batch batch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = false)
    @NotNull(message = "Data Class Tidak Boleh Kosong")
    private Kelas kelas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id", nullable = false)
    @NotNull(message = "Data Mentor Tidak Boleh Kosong")
    private Mentor mentor;

    // plus
    @Column(name="isDeleted")
    private boolean isDeleted;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date created_at;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updated_at;

    public BatchDetail() {
    }

    public BatchDetail(Long id, boolean isDeleted) {
        Date date = new Date();

        this.id = id;
        this.isDeleted = isDeleted;
        this.created_at = date;
        this.updated_at = date;
    }
}
