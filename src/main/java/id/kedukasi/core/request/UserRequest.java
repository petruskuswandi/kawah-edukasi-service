package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRequest implements Serializable {

  private Long id;

  @NotBlank
  @Size(min = 3, max = 20)
  @ApiModelProperty(example = "iam123", required = true)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  @ApiModelProperty(example = "iamEmail@gmail.com", required = true)
  private String email;

  @ApiModelProperty(example = "3", required = true)
  private Integer role;

  @NotBlank
  @Size(min = 6, max = 40)
  @ApiModelProperty(example = "iam123", required = true)
  private String password;

  @Size(max = 50)
  @ApiModelProperty(example = "Nama Lengkap")
  private String namaLengkap;

  @Size(max = 20)
  @ApiModelProperty(example = "08xxxx")
  private String noHp;
  
  private boolean isActive;

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

  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public boolean isIsActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

}
