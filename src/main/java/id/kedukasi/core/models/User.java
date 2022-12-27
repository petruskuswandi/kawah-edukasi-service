package id.kedukasi.core.models;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "users"//,
        // uniqueConstraints = {
        //         @UniqueConstraint(columnNames = "username"),
        //         @UniqueConstraint(columnNames = "email"),
        //         /* Menambahkan unique constraint pada column noHp */
        //         @UniqueConstraint(columnNames = "noHp")
        /*}*/)
@DynamicUpdate
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @Lob
  @Column(name = "profilePicture", updatable = false)
  @ApiModelProperty(hidden = true)
  private byte[] profilePicture;

  @Column(name = "profilePicturePath", updatable = false)
  @ApiModelProperty(hidden = true)
  private String profilePicturePath;

  @NotBlank
  @Size(max = 120)
  private String password;


  @NotBlank
  @Size(max = 50)
  private String namaLengkap;

  @NotBlank
  @Size(max = 20)
  private String noHp;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id")
  private Role role;

  private boolean isLogin;
  private boolean isActive;
  private boolean isVerified;

  @Size(max = 6)
  @ApiModelProperty(hidden = true)
  @Column(name = "tokenVerification", updatable = false)
  private String tokenVerification;

  @Column(name = "created_time", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
  private Date created_time;

  @Column(name = "updated_time")
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
  private Date updated_time;

  @Column(name = "banned", updatable = false)
  private boolean banned;

  @Column(name = "banned_time", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
  private Date banned_time;

  public User() {
  }

  /**
   * Constructor untuk menerima input ketika create User
   */
  public User(String username, String email, String password, String namaLengkap, String noHp, Role role, boolean isActive, String tokenVerification) {
    Date date = new Date();

    this.username = username;
    this.email = email;
    this.password = password;
    this.namaLengkap = namaLengkap;
    this.noHp = noHp;
    this.role = role;
    this.isLogin = false;
    this.isActive = isActive;
    this.isVerified = isActive;
    this.tokenVerification = tokenVerification;
    this.created_time = date;
    this.updated_time = date;
    this.banned = false;
    this.banned_time = null;
  }

  public User(String username, String email, String password,String namaLengkap, String noHp, String tokenVerification,
              Role role, boolean isActive, boolean isLogin) {
    Date date = new Date();

    this.username = username;
    this.email = email;
    this.password = password;
    this.namaLengkap = namaLengkap;
    this.noHp = noHp;
    this.tokenVerification = tokenVerification;
    this.role = role;
    this.isActive = isActive;
    this.isLogin = isLogin;
    this.created_time = date;
    this.updated_time = date;
    this.banned = false;
    this.banned_time = date;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNamaLengkap() {
    return namaLengkap;
  }

  public void setNamaLengkap(String namaLengkap) {
    this.namaLengkap = namaLengkap;
  }

  public String getNoHp() {
    return noHp;
  }

  public void setNoHp(String noHp) {
    this.noHp = noHp;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public boolean isIsLogin() {
    return isLogin;
  }

  public void setIsLogin(boolean isLogin) {
    this.isLogin = isLogin;
  }

  public boolean isIsActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public boolean isVerified() {
      return isVerified;
  }

  public void setVerified(boolean isVerified) {
      this.isVerified = isVerified;
  }

  public String getTokenVerification() {
    return tokenVerification;
  }

  public void setTokenVerification(String tokenVerification) {
    this.tokenVerification = tokenVerification;
  }

  public byte[] getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(byte[] profilePicture) {
    this.profilePicture = profilePicture;
  }

  public Date getCreated_time() {
    return created_time;
  }

  public void setCreated_time(Date created_time) {
    this.created_time = created_time;
  }

  public Date getUpdated_time() {
    return updated_time;
  }

  public void setUpdated_time(Date updated_time) {
    this.updated_time = updated_time;
  }

  public boolean isBanned() {
    return banned;
  }

  public void setBanned(boolean banned) {
    this.banned = banned;
  }

  public Date getBanned_time() {
    return banned_time;
  }

  public void setBanned_time(Date banned_time) {
    this.banned_time = banned_time;
  }

  public String getProfilePicturePath() {
    return profilePicturePath;
  }

  public void setProfilePicturePath(String profilePicturePath) {
    this.profilePicturePath = profilePicturePath;
  }

}
