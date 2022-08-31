package id.kedukasi.core.controller;

import id.kedukasi.core.exception.TokenRefreshException;
import id.kedukasi.core.models.LoginRequest;
import id.kedukasi.core.models.RefreshToken;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.SignupRequest;
import id.kedukasi.core.models.TokenRefreshRequest;
import id.kedukasi.core.models.TokenRefreshResponse;
import id.kedukasi.core.service.UserService;
import id.kedukasi.core.serviceImpl.RefreshTokenService;
import id.kedukasi.core.utils.StringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  UserService service;

  @Autowired
  StringUtil stringUtil;

  @Autowired
  HttpServletRequest request;

  @Autowired
  RefreshTokenService refreshTokenService;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);

    return service.signIn(loginRequest, uri);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    return service.createUser(signUpRequest);
  }

  @PostMapping("/signout")
  public Result logout(@RequestParam(value = "id", defaultValue = "0", required = true) long id) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);

    return service.signOut(id, uri);
  }

  @PostMapping("/active")
  public Result active(
      @RequestParam(value = "id", defaultValue = "0", required = true) long id,
      @RequestParam(value = "tokenVerification", defaultValue = "", required = true) String tokenVerification
  ) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);

    return service.active(id, tokenVerification, uri);
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    return service.refreshToken(request);
  }
}
