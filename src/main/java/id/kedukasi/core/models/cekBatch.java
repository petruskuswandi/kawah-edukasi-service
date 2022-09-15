//package id.kedukasi.core.models;
//
//import org.hibernate.annotations.DynamicUpdate;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;
//import java.io.Serializable;
//import java.util.Date;
//
//@Entity
//@Table(name = "batch", uniqueConstraints = {
//        @UniqueConstraint(columnNames = "batchname"),
//})
//
//@DynamicUpdate
//public class Batch implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank
//    @Size(max = 30)
//    private String batchname;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinTable(name = "kelas",
//            joinColumns = @JoinColumn(name = "batch"),
//            inverseJoinColumns = @JoinColumn(name = "kelas"))
//    private int classId;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinTable(name = "kelas",
//            joinColumns = @JoinColumn(name = "batch"),
//            inverseJoinColumns = @JoinColumn(name = "kelas"))
//    private int mentorId;
//
//
//
//    @Column(name = "started_time", updatable = false)
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date starDate;
//
//
//    @Column(name = "ended_time", updatable = false)
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date endDate;
//
//
//    @Column(name = "created_time", updatable = false)
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date created_time;
//
//    @Column(name = "updated_time")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date updated_time;
//
//    @Column(name = "banned", updatable = false)
//    private boolean banned;
//
//    @Column(name = "banned_time", updatable = false)
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date banned_time;
//
//
//    public Batch(){
//
//    }
//
//    public Batch(Long id, String batchname, int classId, int mentorId, Date starDate, Date endDate, Date created_time, Date updated_time, boolean banned, Date banned_time) {
//        this.id = id;
//        this.batchname = batchname;
//        this.classId = classId;
//        this.mentorId = mentorId;
//        this.starDate = starDate;
//        this.endDate = endDate;
//        this.created_time = created_time;
//        this.updated_time = updated_time;
//        this.banned = banned;
//        this.banned_time = banned_time;
//    }
//
//    public Date getStarDate() {
//        return starDate;
//    }
//
//    public void setStarDate(Date starDate) {
//        this.starDate = starDate;
//    }
//
//    public Date getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getBatchname() {
//        return batchname;
//    }
//
//    public void setBatchname(String batchname) {
//        this.batchname = batchname;
//    }
//
//    public int getClassId() {
//        return classId;
//    }
//
//    public void setClassId(int classId) {
//        this.classId = classId;
//    }
//
//    public int getMentorId() {
//        return mentorId;
//    }
//
//    public void setMentorId(int mentorId) {
//        this.mentorId = mentorId;
//    }
//
//    public Date getCreated_time() {
//        return created_time;
//    }
//
//    public void setCreated_time(Date created_time) {
//        this.created_time = created_time;
//    }
//
//    public Date getUpdated_time() {
//        return updated_time;
//    }
//
//    public void setUpdated_time(Date updated_time) {
//        this.updated_time = updated_time;
//    }
//
//    public boolean isBanned() {
//        return banned;
//    }
//
//    public void setBanned(boolean banned) {
//        this.banned = banned;
//    }
//
//    public Date getBanned_time() {
//        return banned_time;
//    }
//
//    public void setBanned_time(Date banned_time) {
//        this.banned_time = banned_time;
//    }
//}
