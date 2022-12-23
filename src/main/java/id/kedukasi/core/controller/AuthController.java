package id.kedukasi.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.kedukasi.core.request.ChangePasswordRequest;
import id.kedukasi.core.request.LoginRequest;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.request.SignupRequest;
import id.kedukasi.core.request.TokenRefreshRequest;
import id.kedukasi.core.service.UserService;
import id.kedukasi.core.utils.StringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

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
  UserRepository userRepository;

  @Autowired
  PasswordEncoder encoder;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(
          @Valid 
          @RequestBody LoginRequest loginRequest) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);
    
    Boolean isActive = userRepository.findByEmail(loginRequest.getEmail()).get().isIsActive();

    Result result = new Result();
    if(isActive.equals(false)){
      result.setMessage("Akun Belum Aktif, Silahkan Aktivasi Akun Terlebih Dahulu untuk Dapat Sign In!");
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity
              .badRequest()
              .body(result);
    }
    
    return service.signIn(loginRequest, uri);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(
          @Valid 
          @RequestParam(value = "user activation", defaultValue = "Aktif", required = true) String userActivation,
          @RequestBody SignupRequest signUpRequest) {//Password pada Signup Request di generate auto pada UserServiceImpl dan ikut disend pada email
    service.createUser(signUpRequest); //Format Isian untuk memberikan id, token, email, dan password disamakan isiannya dengan Forgot Password di UserServiceImpl

    Long id = userRepository.findByEmail(signUpRequest.getEmail()).get().getId();
    String tokenVerification = userRepository.findByEmail(signUpRequest.getEmail()).get().getTokenVerification();
    String statusAktif = "Aktif";
    statusAktif = userActivation;

    Result result = new Result();
    if(statusAktif.toUpperCase().equals("AKTIF")){
      String uri = stringUtil.getLogParam(request);
      logger.info(uri);

      service.active(id, tokenVerification, uri);

      result.setMessage("User telah berhasil terdaftar dan akun sudah teraktivasi!");
      result.setCode(HttpStatus.OK.value());
      return ResponseEntity.ok(result);  
    }

    result.setMessage("User telah berhasil terdaftar, tetapi akun belum teraktivasi!");
    result.setCode(HttpStatus.OK.value());
    return ResponseEntity.ok(result);
  }

  @PostMapping("/signout")
  public Result logout(
          @RequestParam(value = "id", defaultValue = "0", required = true) long id) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);

    return service.signOut(id, uri);
  }

  @GetMapping("/active")
  public Result active(
          @RequestParam(value = "id", defaultValue = "0", required = true) long id,
          @RequestParam(value = "tokenVerification", defaultValue = "", required = true) String tokenVerification) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);

    return service.active(id, tokenVerification, uri);
  }

  @PatchMapping("/change_password")
  public ResponseEntity<?> changePassword(
          @RequestParam(value = "id", defaultValue = "0", required = true) long id,
          @RequestParam(value = "password lama", defaultValue = "", required = true) String oldPassword,
          @RequestParam(value = "password baru", defaultValue = "", required = true) String newPassword) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);

    String password = userRepository.findById(id).getPassword();

    Result result = new Result();
    if(!encoder.matches(oldPassword, password)){
      result.setMessage("Password Lama yang Dimasukkan Tidak Sesuai");
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity
              .badRequest()
              .body(result);
    }
    return service.changePassword(id, newPassword, uri);
  }

  @PostMapping("/change_password_forgot")
  public ResponseEntity<?> changePasswordForget(
          @RequestBody ChangePasswordRequest param) throws JsonProcessingException {
    return service.changePasswordForgot(param);
  }

  @PostMapping("/forgot_password")
  public ResponseEntity<?> forgotPassword(
          @RequestParam(value = "email", defaultValue = "", required = true) String email) throws IOException {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);

    return service.forgotPassword(email, uri);
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(
          @Valid 
          @RequestBody TokenRefreshRequest request) {
    return service.refreshToken(request);
  }
}
