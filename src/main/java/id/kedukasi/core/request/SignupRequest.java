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

  private Integer role;

  @NotBlank
  @Size(min = 6, max = 40)
  @ApiModelProperty(example = "iam123", required = true)
  private String password;

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

  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }
  
}