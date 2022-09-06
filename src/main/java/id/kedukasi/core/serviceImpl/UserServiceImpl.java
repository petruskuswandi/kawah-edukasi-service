package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumRole;
import id.kedukasi.core.exception.TokenRefreshException;
import id.kedukasi.core.models.EmailDetails;
import id.kedukasi.core.response.JwtResponse;
import id.kedukasi.core.request.LoginRequest;
import id.kedukasi.core.models.RefreshToken;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.Role;
import id.kedukasi.core.models.User;
import id.kedukasi.core.repository.RoleRepository;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.request.SignupRequest;
import id.kedukasi.core.request.TokenRefreshRequest;
import id.kedukasi.core.response.TokenRefreshResponse;
import id.kedukasi.core.service.EmailService;
import id.kedukasi.core.service.FilesStorageService;
import id.kedukasi.core.service.UserService;
import id.kedukasi.core.utils.GlobalUtil;
import id.kedukasi.core.utils.JwtUtils;
import id.kedukasi.core.utils.StringUtil;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
      Map items = new HashMap();
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

    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()), StringUtil.getRandomNumberString());

    Role role = roleRepository.findById(signUpRequest.getRole()).orElse(null);
    if (role == null) {
      Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      role = userRole;
    } else {
      switch (role.getId()) {
        case 1:
          Role adminRole = roleRepository.findByName(EnumRole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          role = adminRole;
          break;
        case 2:
          Role modRole = roleRepository.findByName(EnumRole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          role = modRole;
          break;
        default:
          Role userRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          role = userRole;
      }
    }
    user.setRoles(role);
    user.setIsActive(false);
    user.setIsLogin(false);
    User userResult = userRepository.save(user);

    if (userResult != null) {
      sendActivationEmail(userResult.getId(), userResult.getTokenVerification(), userResult.getEmail());
    }

    result.setMessage("User registered successfully!");
    result.setCode(HttpStatus.OK.value());
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<?> signIn(LoginRequest loginRequest, String uri) {
    result = new Result();
    User getUser = userRepository.findByEmail(loginRequest.getEmail()).orElse(new User());
    if (getUser.getUsername() != null) {
      Date dateNow = new Date();
      Date dateExpired = new Date((dateNow).getTime() + jwtExpirationMs);

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
    } else {
      result.setSuccess(true);
      result.setCode(HttpStatus.BAD_REQUEST.value());
      result.setMessage("Email not registered");
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
  public ResponseEntity<?> updateUser(User user) {
    result = new Result();
    try {
      User checkUserEmail = userRepository.findByEmail(user.getEmail()).orElse(new User());
      if (checkUserEmail.getUsername() != null && !Objects.equals(user.getId(), checkUserEmail.getId())) {
        result.setMessage("Error: Email is already in use!");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
            .badRequest()
            .body(result);
      }

      User checkUserUsername = userRepository.findByUsername(user.getUsername()).orElse(new User());
      if (checkUserUsername.getUsername() != null && !Objects.equals(user.getId(), checkUserUsername.getId())) {
        result.setMessage("Error: Username is already taken!");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
            .badRequest()
            .body(result);
      }

      Role role = user.getRoles();

      if (role == null) {
        Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        role = userRole;
      } else {

        switch (role.getId()) {
          case 3:
            Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            role = adminRole;
            break;
          case 2:
            Role modRole = roleRepository.findByName(EnumRole.ROLE_MODERATOR)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            role = modRole;
            break;
          default:
            Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            role = userRole;
        }
      }
      user.setPassword(encoder.encode(user.getPassword()));
      user.setRoles(role);
      userRepository.save(user);

      result.setMessage("User update successfully!");
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
  public ResponseEntity<?> forgotPassword(String email, String uri) {
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

    String body = "<html>"
        + "<body>"
        + "Click <a href=\"" + urlForgotPassword + "\">here</a> to reset your password."
        + "</body>"
        + "</html>";
    emailDetails.setMsgBody(body);
    emailDetails.setRecipient(email);
    logger.info(">>>> send email");
    emailService.sendMailWithAttachment(emailDetails);

    return ResponseEntity.ok(new Result());
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
