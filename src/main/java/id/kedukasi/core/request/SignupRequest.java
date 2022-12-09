package id.kedukasi.core.request;


import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

public class SignupRequest {

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
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%#*?&])[A-Za-z\\d@$!%#*?&]{8,}$",message = "Password must be longer than 8 characters,use at least 1 uppercase letter,spesial characters and not contain spaces!!")
  private String password;

  @NotBlank
  @Size(max = 50)
  @ApiModelProperty(example = "Nama Lengkap", required = true)
  private String namaLengkap;

  @NotBlank
  @Size(max = 20)
  @ApiModelProperty(example = "08xxxx", required = true)
  private String noHp;

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
  
}
