package id.kedukasi.core.response;

import id.kedukasi.core.models.Role;

public class JwtResponse {

  private String token;
  private String type = "Bearer";
  private String refreshToken;
  private Long id;
  private String username;
  private String email;
  private Role role;
  private long expired;

  public JwtResponse(String accessToken, String refreshToken, Long id, String username,
      String email, Role role, long expired) {
    this.token = accessToken;
    this.refreshToken = refreshToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.role = role;
    this.expired = expired;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public long getExpired() {
    return expired;
  }

  public void setExpired(long expired) {
    this.expired = expired;
  }

}
