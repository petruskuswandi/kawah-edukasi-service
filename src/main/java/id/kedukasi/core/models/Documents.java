package id.kedukasi.core.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")
@DynamicUpdate
public class Documents implements Serializable{

    @Id
    @SequenceGenerator(
            name = "documentsSequence",
            sequenceName = "documents_id_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "documentsSequence"
    )
    private int id;

    @Column(name = "path_name", nullable = false)
    private String pathName;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @JsonIgnoreProperties({"created_at", "updated_at", "deleted", "description"})
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @JsonIgnoreProperties({"email", "profilePicture", "profilePicturePath",
            "password", "namaLengkap", "noHp", "role", "isLogin", "isActive", "tokenVerification",
            "created_time", "updated_time", "banned", "banned_time", "verified"})
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_banned")
    private boolean banned;

    @Column(name = "created_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    private Date createdTime = new Date();

    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    private Date updatedTime = new Date();

    @Column(name = "banned_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    private Date bannedTime;

}