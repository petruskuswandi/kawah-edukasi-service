package id.kedukasi.core.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.kedukasi.core.enums.EnumRole;
import id.kedukasi.core.exception.TokenRefreshException;
import id.kedukasi.core.models.EmailDetails;
import id.kedukasi.core.request.*;
import id.kedukasi.core.response.JwtResponse;
import id.kedukasi.core.models.RefreshToken;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.Role;
import id.kedukasi.core.models.User;
import id.kedukasi.core.repository.RoleRepository;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.response.TokenRefreshResponse;
import id.kedukasi.core.service.EmailService;
import id.kedukasi.core.service.FilesStorageService;
import id.kedukasi.core.service.UserService;
import id.kedukasi.core.utils.GlobalUtil;
import id.kedukasi.core.utils.JwtUtils;
import id.kedukasi.core.utils.StringUtil;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import id.kedukasi.core.utils.ValidatorUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  StringUtil stringUtil;

  @Autowired
  ValidatorUtil validator;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Autowired
  EmailService emailService;

  @Autowired
  FilesStorageService storageService;

  @Autowired
  GlobalUtil globalUtil;

  @Value("${app.expired.token.forgot-password}")
  private long jwtExpiredTokenForgotPassword;


  @Value("${kedukasi.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${app.url.forgot-password}")
  private String urlForgotPassword;

  private Result result;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public Result getAllUser(String uri) {
    result = new Result();
    try {
      Map<String, List<User>> items = new HashMap<>();
      items.put("items", userRepository.findAll());
      result.setData(items);
    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
    }

    return result;
  }

  @Override
  public Result getUserById(long id, String uri) {
    result = new Result();
    try {
      User user = userRepository.findById(id);
      if (user == null) {
        result.setSuccess(false);
        result.setMessage("cannot find user");
        result.setCode(HttpStatus.BAD_REQUEST.value());
      } else {
        Map items = new HashMap();
        items.put("items", userRepository.findById(id));
        result.setData(items);
      }

    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
    }

    return result;
  }

  @Override
  public ResponseEntity<?> createUser(SignupRequest signUpRequest) {
    result = new Result();



    if (!validator.isPhoneValid(signUpRequest.getNoHp())) {
      result.setMessage("Error: invalid phone number!");
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity
              .badRequest()
              .body(result);
    }

    if(!validator.isEmailValid(signUpRequest.getEmail())) {
      result.setMessage("Error: invalid email format!");
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity
              .badRequest()
              .body(result);
    }


    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      result.setMessage("Error: Email is already in use!");
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity
              .badRequest()
              .body(result);
    }
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      result.setMessage("Error: Usename is already taken!");
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity
              .badRequest()
              .body(result);
    }

    Role role = roleRepository.findById(signUpRequest.getRole()).orElse(null);
    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()),signUpRequest.getNamaLengkap(),
            signUpRequest.getNoHp(), StringUtil.getRandomNumberString(), role, false, false);
    User userResult = userRepository.save(user);

//    if (userResult != null) {
//      sendActivationEmail(userResult.getId(), userResult.getTokenVerification(), userResult.getEmail());
//    }

    result.setMessage("User registered successfully!");
    result.setCode(HttpStatus.OK.value());
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<?> signIn(LoginRequest loginRequest, String uri) {
    result = new Result();
    // email validation
    if(!validator.isEmailValid(loginRequest.getEmail())){
      result.setMessage("Email not valid!!");
      result.setCode(400);
      result.setSuccess(false);
      return ResponseEntity.badRequest().body(result);
    }

    User getUser = userRepository.findByEmail(loginRequest.getEmail()).orElse(new User());

    if(validator.isEmailValid(loginRequest.getEmail()) && getUser.getUsername() == null){
      result.setSuccess(true);
      result.setCode(HttpStatus.BAD_REQUEST.value());
      result.setMessage("Email not registered");
      return ResponseEntity.ok(result);
    }

    // password validation
    if(!validator.isPasswordValid(loginRequest.getPassword())){
      result.setMessage("Password must be longer than 8 characters,use at least 1 uppercase letter,spesial characters and not contain spaces!!");
      result.setCode(400);
      result.setSuccess(false);
      return ResponseEntity.badRequest().body(result);
    }

    if (getUser.getUsername() != null) {
      Date dateNow = new Date();
      Date dateExpired = new Date((dateNow).getTime() + jwtExpirationMs);

      if ( validator.isPasswordValid(loginRequest.getPassword()) && !encoder.matches(loginRequest.getPassword(), getUser.getPassword())){
        result.setSuccess(true);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setMessage("Password salah");
        return ResponseEntity.ok(result);
      }
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(getUser.getUsername(), loginRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication, dateNow, dateExpired);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      Role role = roleRepository.findByName(EnumRole.valueOf(userDetails.getAuthorities().stream().findAny().get().getAuthority())).orElse(new Role());
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

      result.setSuccess(true);
      result.setMessage("success");
      result.setData(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(),
              userDetails.getEmail(), role, dateExpired.getTime()));

      userRepository.setIsLogin(true, userDetails.getId());
    }

    return ResponseEntity.ok(result);
  }

  @Override
  public Result signOut(long id, String uri) {
    result = new Result();
    try {
      int status = userRepository.setIsLogin(false, id);
      if (status == 1) {
        result.setMessage("success");
      } else {
        result.setMessage("failed");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
      }

    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
    }

    return result;
  }

  @Override
  public Result active(long id, String tokenVerification, String uri) {
    result = new Result();
    try {
      int status = userRepository.setIsActive(true, id, tokenVerification);
      if (status == 1) {
        result.setMessage("user is actived");
      } else {
        result.setMessage("failed");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
      }

    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
    }

    return result;
  }

  @Override
  public ResponseEntity<?> changePassword(long id, String password, String uri) {
    result = new Result();

    int resultModel = userRepository.changePassword(encoder.encode(password), id);
    if (resultModel == 1) {
      return ResponseEntity.ok(result);
    } else {
      return ResponseEntity.badRequest().body(result);
    }
  }

  @Override
  public ResponseEntity<?> refreshToken(TokenRefreshRequest tokenRefreshRequest) {
    String requestRefreshToken = tokenRefreshRequest.getRefreshToken();
    return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getUsername());
              return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
            .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                    "Refresh token is not in database!"));
  }

  @Override
  public ResponseEntity<Result> updateUser(UserRequest userRequest) {
    result = new Result();
    try {
      User checkUserEmail = userRepository.findByEmail(userRequest.getEmail()).orElse(new User());
      if (checkUserEmail.getEmail()!= null && !Objects.equals(userRequest.getId(), checkUserEmail.getId())) {
        result.setMessage("Error: Email is already in use!");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      User checkUserUsername = userRepository.findByUsername(userRequest.getUsername()).orElse(new User());
      if (checkUserUsername.getUsername() != null && !Objects.equals(userRequest.getId(), checkUserUsername.getId())) {
        result.setMessage("Error: Username is already taken!");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      Role role = roleRepository.findById(userRequest.getRole()).orElse(null);
      if (role == null) {
        result.setMessage("Error: Role not found!");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      if (!validator.isPhoneValid(userRequest.getNoHp())) {
        result.setMessage("Error: invalid phone number!");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      User user = new User(userRequest.getUsername(), userRequest.getEmail(),
              encoder.encode(userRequest.getPassword()),userRequest.getNamaLengkap(), userRequest.getNoHp(),
              StringUtil.getRandomNumberString(), role, userRequest.isIsActive(), true);

      user.setId(userRequest.getId());
      userRepository.save(user);

      result.setMessage(userRequest.getId() == 0 ? "User registered successfully!" : "User updated successfully!");
      result.setCode(HttpStatus.OK.value());
    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
    }

    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<?> updateProfilePictureBlob(long id, MultipartFile profilePicture, String uri) {
    result = new Result();
    try {
      userRepository.updateProfilePicture(IOUtils.toByteArray(profilePicture.getInputStream()), id);
    } catch (IOException e) {
      logger.error(stringUtil.getError(e));
    }

    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<?> deleteUser(boolean banned, long id, String uri) {
    result = new Result();
    try {
      userRepository.deleteUser(banned, id);
    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
    }

    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<?> updateProfilePictureFolder(long id, MultipartFile profilePicture, String uri) {
    result = new Result();
    try {
      String filename = String.valueOf("profie_picture_" + id).concat(".")
              .concat(globalUtil.getExtensionByStringHandling(profilePicture.getOriginalFilename()).orElse(""));
      String filenameResult = storageService.save(profilePicture, filename);
      userRepository.setProfilePicturePath(filenameResult, id);
      result.setMessage("succes to save file ".concat(profilePicture.getOriginalFilename()));
    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
    }

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  private void sendActivationEmail(long id, String tokenVerification, String receiver) {
    EmailDetails emailDetails = new EmailDetails();
    emailDetails.setSubject("Activate User");

    String url = "";
    String body = "<html>"
            + "<body>"
            + "Click <a href=\"http://localhost:8880/api/auth/active?id=" + id + "&tokenVerification="
            + tokenVerification + "\">here</a> to activate your account."
            + "</body>"
            + "</html>";
    emailDetails.setMsgBody(body);
    emailDetails.setRecipient(receiver);
    logger.info(">>>> send email");
    emailService.sendMailWithAttachment(emailDetails);
  }

  @Override
  public ResponseEntity<?> forgotPassword(String email, String uri) throws IOException {
    result = new Result();

    User checkUserEmail = userRepository.findByEmail(email).orElse(new User());
    if (checkUserEmail.getUsername() == null) {
      result.setMessage("Error: Email has not been registered!");
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity
              .badRequest()
              .body(result);
    }
    EmailDetails emailDetails = new EmailDetails();
    emailDetails.setSubject("Forgot Password");

    Date dateNow = new Date();
    // 10 menit
    String tokenForgotPassword = jwtUtils.generateTokenFromUsernameWithExpired(checkUserEmail.getUsername(),jwtExpiredTokenForgotPassword);
    long idUser = checkUserEmail.getId();
    String password = checkUserEmail.getPassword();

    /*
        Body HTML Message Email
     */
    String body = "<!DOCTYPE html>\n" +
            "<html lang=\"en\" xmlns:th=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "  <title>Template Email - Change Password</title>\n" +
            "  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n" +
            "  <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n" +
            "  <link href=\"https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap\" rel=\"stylesheet\">\n" +
            "  <style>\n" +
            "    * {\n" +
            "      margin: 0;\n" +
            "      padding: 0;\n" +
            "      box-sizing: border-box;\n" +
            "      font-family: 'Poppins', sans-serif;\n" +
            "    }\n" +
            "\n" +
            "    p {\n" +
            "      font-size: 22px;\n" +
            "      font-weight: 400;\n" +
            "      line-height: 36px;\n" +
            "    }\n" +
            "\n" +
            "    body {\n" +
            "      display: flex;\n" +
            "      justify-content: center;\n" +
            "      align-items: center;\n" +
            "      height: 100vh;\n" +
            "    }\n" +
            "\n" +
            "    body > div > div > div:nth-of-type(2) a:hover {\n" +
            "      transform: translateY(-1px);\n" +
            "    }\n" +
            "  </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <div style=\"width: 100%; max-width: 932px; height: 599px; border: 2px solid #E2E2E2; border-radius: 8px;\">\n" +
            "    <div style=\"padding: 57px;\">\n" +
            "      <div style=\"display: flex; justify-content: space-between; align-items: center; margin-bottom: 23px;\">\n" +
            "        <div>\n" +
            "          <p style=\"font-size: 24px; font-weight: 600;\">Halo "+checkUserEmail.getNamaLengkap()+",</p>\n" +
            "          <p style=\"font-size: 20px; font-weight: 300;\">berikut adalah link untuk reset password.</p>\n" +
            "        </div>\n" +
            "        <div>\n" +
            "          <img src=\"cid:logo\" alt=\"Logo Kawah Edukasi\">\n" +
            "        </div>\n" +
            "      </div>\n" +
            "      <hr style=\"margin-bottom: 30px;\">\n" +
            "      <div style=\"display: flex; flex-direction: column;\">\n" +
            "        <p>\n" +
            "          Permintaan untuk reset password Kawah Edukasi Anda telah dibuat.\n" +
            "          Jika Anda tidak membuat permintaan, abaikan saja email ini. Jika\n" +
            "          Anda memang membuat permintaan, harap segera reset password\n" +
            "          Anda :\n" +
            "        </p><br>\n" +
            "        <a \n" +
            "          href="+urlForgotPassword+idUser+"?token="+tokenForgotPassword+" \n" +
            "          target=\"_blank\"\n" +
            "          style=\"align-self: center; width: 399px; height: 55px; margin: 35px 0; padding: 10px; color: white; text-align: center; text-decoration: none; font-size: 24px; font-weight: 600; background-color: #0D9CA8; cursor: pointer; border: none; border-radius: 8px;\"\n" +
            "          >Ubah Password</a><br>\n" +
            "        <p style=\"font-weight: 500;\">Kawah Edukasi</p>\n" +
            "        <p>Support Team</p>\n" +
            "      </div>\n" +
            "      <div>\n" +
            "        <p style=\"font-weight: 500; margin-top: 25px; text-align: center;\">2022 &copy; Kawah Edukasi.</p>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</body>\n" +
            "</html>";
    //logger.info(body);
    emailDetails.setMsgBody(body);
    emailDetails.setRecipient(email);
    logger.info(">>>> send email");
    //emailService.sendMailWithAttachment(emailDetails);
    String urlendpoint = urlForgotPassword+"?id="+idUser+"&password="+password+"&token="+tokenForgotPassword;
    emailService.sendMailForgotPassword(emailDetails,checkUserEmail,urlendpoint);
    return ResponseEntity.ok(new Result());
  }

  @Override
  public ResponseEntity<?> changePasswordForgot(ChangePasswordRequest param) throws JsonProcessingException {
    result = new Result();
    if(!jwtUtils.validateJwtToken(param.getToken()) || param.getToken() == null){
      result.setCode(400);
      result.setMessage("Token Is Invalid");
      return ResponseEntity.badRequest().body(result);
    }

    // password validation
    if(!validator.isPasswordValid(param.getPassword())){
      result.setMessage("Password must be longer than 8 characters,use at least 1 uppercase letter,spesial characters and not contain spaces!!");
      result.setCode(400);
      result.setSuccess(false);
      return ResponseEntity.badRequest().body(result);
    }

    int resultModel = userRepository.changePassword(encoder.encode(param.getPassword()), param.getId());
    if (resultModel == 1) {
      result.setCode(200);
      result.setMessage("Password Succesfully Updated");
      return ResponseEntity.ok(result);
    } else {
      result.setCode(200);
      result.setMessage("Password Failed Updated");
      return ResponseEntity.badRequest().body(result);
    }
  }
//
//  private String getJwtActiveEmail() {
//    jwtToken = JWT.create()
//        .withIssuer(issuer)
//        .withSubject(subject)
//        .withClaim("u_id", id)
//        .withClaim("u_email", email)
//        .withClaim("city_code", location)
//        .withIssuedAt(date)
//        .withExpiresAt(expTime)
//        .sign(algorithm);
//  }

}
