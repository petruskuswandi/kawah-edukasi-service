package id.kedukasi.core.models;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "users",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email")
    })
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

  @NotBlank
  @Size(max = 120)
  private String password;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Role roles;

  private boolean isLogin;
  private boolean isActive;

  @Size(max = 6)
  @ApiModelProperty(hidden = true)
  @Column(name = "tokenVerification", updatable = false)
  private String tokenVerification;

  public User() {
  }

  public User(String username, String email, String password, String tokenVerification) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.tokenVerification = tokenVerification;
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

  public Role getRoles() {
    return roles;
  }

  public void setRoles(Role roles) {
    this.roles = roles;
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

}
