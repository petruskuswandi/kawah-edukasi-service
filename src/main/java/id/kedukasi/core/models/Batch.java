package id.kedukasi.core.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "batches",uniqueConstraints = {
        @UniqueConstraint(columnNames = "batchname"),
})

@DynamicUpdate
public class Batch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String batchname;

    @NotBlank
    @Size(max = 100)
    private String description;


    // class dan mentor
//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "classes",
//            joinColumns = @JoinColumn(name = "classname"),
//            inverseJoinColumns = @JoinColumn(name = "classname"))
//    private String classname;
//
//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "mentores",
//            joinColumns = @JoinColumn(name = "mentorname"),
//            inverseJoinColumns = @JoinColumn(name = "mentorname"))
//    private String mentorname;

    @Column(name = "banned")
    private boolean banned;

    @Column(name = "banned_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date banned_time;

    // menambahkan stadate & Endedate
    @Column(name = "started_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedtime;

    @Column(name = "ended_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endedtime;

    @Column(name = "created_by", updatable = false)
    private String created_by;

    @Column(name = "created_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_time;

    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_time;

    public Batch() {
    }


    public Batch(String batchname, String description,Date startedtime, Date endedtime) {
        Date date = new Date();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        this.batchname = batchname;
        this.description = description;
        this.startedtime = startedtime;
        this.endedtime = endedtime;
        this.created_by = auth.getName();
        this.created_time = date;
        this.updated_time = date;
        this.banned = false;
        this.banned_time = date;
    }
}
