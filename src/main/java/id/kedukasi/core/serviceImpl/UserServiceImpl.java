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

  @Value("${app.url.base}")
  private String urlBase;

  @Value("${app.url.forgot-password}")
  private String urlForgotPassword;

  @Value("${app.url.active-user}")
  private String urlActivation;

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
  public Result getUserData(String uri, String search, Integer limit, Integer page) {
    result = new Result();

    Integer total = userRepository.totalUnbannedUser();
    if (total == null) { total = 0; }

    int jumlahPage = (int) Math.ceil(total.intValue() / (double) limit);
    
    // limit 0 or negative integer
    if (limit < 1) { limit = 1; }
    // jumlah page 0 or less, set to 1
    if (jumlahPage < 1) { jumlahPage = 1; }
    // page greater then jumlah page
    if (page > jumlahPage) { page = jumlahPage; }
    // page 0 or negative integer
    if (page < 1) { page = 1; }
    // search in null condition
    if (search == null) { search = ""; }

    try {
      Map<String, Object> items = new HashMap<>();
      List<User> user = userRepository.findUserData(search.toLowerCase(), limit.intValue(), page.intValue());
      List<SubUser> subUser = new ArrayList<>();
      for (int i = 0; i < user.size(); i++) {
        User dataUser = user.get(i);
        SubUser su = new SubUser(dataUser.getId(), dataUser.getNamaLengkap(),
                     dataUser.getEmail(), dataUser.getNoHp(), dataUser.getRole(),
                     dataUser.isIsActive());
        subUser.add(su);
      }
      items.put("items", subUser);
      items.put("totalDataResult", subUser.size());
      items.put("totalData", total);
      result.setData(items);
    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
      result.setSuccess(false);
      result.setMessage(e.getMessage());
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return result;
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
        Map<String, User> items = new HashMap<>();
        items.put("items", userRepository.findById(id));
        result.setData(items);
      }

    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
      result.setSuccess(false);
      result.setMessage(e.getMessage());
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return result;
    }

    return result;
  }

  @Override
  public ResponseEntity<?> createUser(SignupRequest signUpRequest) {
    result = new Result();

    try {
      String nama = signUpRequest.getNamaLengkap();
      if (nama.isBlank() || nama.isEmpty()) {
        result.setMessage("Error: Nama lengkap harus diisi!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(result);
      }

      String email = signUpRequest.getEmail();
      if (email.isBlank() || email.isEmpty()) {
        result.setMessage("Error: Email harus diisi!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(result);
      }

      String nomorHp = signUpRequest.getNoHp();
      if (nomorHp.isBlank() || nomorHp.isEmpty()) {
        result.setMessage("Error: Nomor telepon harus diisi!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(result);
      }

      if (!validator.isEmailValid(signUpRequest.getEmail())) {
        result.setMessage("Error: Format email tidak sesuai!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      if (!validator.isPhoneValid(signUpRequest.getNoHp())) {
        result.setMessage("Error: Nomor telepon tidak sesuai!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      Integer checkUserEmail = userRepository.existsByEmail(signUpRequest.getEmail());
      if (checkUserEmail != null && checkUserEmail > 0) {
        result.setMessage("Error: Email sudah digunakan!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      Integer checkUserNoHp = userRepository.existsByNoHp(signUpRequest.getNoHp());
      if (checkUserNoHp != null && checkUserNoHp > 0) {
          result.setMessage("Error: Nomor telepon sudah digunakan!");
          result.setSuccess(false);
          result.setCode(HttpStatus.BAD_REQUEST.value());
          return ResponseEntity
                  .badRequest()
                  .body(result);
      }

      Optional<Role> role = roleRepository.findById(signUpRequest.getRole());
      if (!role.isPresent()) {
        result.setMessage("Error: Role tidak ditemukan!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      if (signUpRequest.getIsActive() == null) {
        signUpRequest.setIsActive(false);
      }

      String password = StringUtil.alphaNumericString();
      String username = signUpRequest.getEmail().split("@")[0];
      String token =  StringUtil.getRandomNumberString();

      // Variable tampungan username
      String tempUsername = username;
      // Lakukan perulangan jika username sudah ada
      while (userRepository.existsByUsername(username) > 0) {
        Random rnd = new Random();
        String newUsername = tempUsername + rnd.nextInt(100);
        username = newUsername;
      }
      
      // Lakukan perulangan jika ternyata token sudah digunakan
      while (userRepository.existsByToken(token) > 0) {
        token = StringUtil.getRandomNumberString();
      }

      User user = new User(username, signUpRequest.getEmail(), encoder.encode(password), signUpRequest.getNamaLengkap(),
                          signUpRequest.getNoHp(), role.get(), signUpRequest.getIsActive(), token);
      User userResult = userRepository.save(user);

      if (userResult != null) {
        sendActivationEmail(userResult.getEmail(), password, userResult.getTokenVerification());
      }

      result.setMessage("User berhasil dibuat.");
      result.setCode(HttpStatus.OK.value());
    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
      result.setSuccess(false);
      result.setMessage(e.getMessage());
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity.badRequest().body(result);
    }
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
      result.setSuccess(false);
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
  public Result active(String tokenVerification, String uri) {
    result = new Result();
    try {
      // variable set bisa dijadikan parameter pada method
      // jika dijadikan parameter, maka method ini bisa digunakan
      // untuk aktifkan atau non-aktifkan akun
      boolean set = true;
      boolean cek = userRepository.isUserVerified(tokenVerification);

      int status = userRepository.setIsActive(true, tokenVerification);

      // // cek apakah token sudah diverifikasi atau belum
      if (set && cek == false) {
        userRepository.setIsVerified(true, tokenVerification);
      }

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
      Optional<User> userLama = userRepository.findById(userRequest.getId());

      if (!userLama.isPresent()) {
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setSuccess(false);
        result.setMessage("Error: Id user tidak ditemukan!");
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      if (!validator.isPhoneValid(userRequest.getNoHp())) {
        result.setMessage("Error: Format telepon tidak sesuai!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      if (!validator.isEmailValid(userRequest.getEmail())) {
        result.setMessage("Error: Format email tidak sesuai!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      User checkUserEmail = userRepository.findByEmail(userRequest.getEmail()).orElse(new User());
      if (checkUserEmail.getEmail()!= null && checkUserEmail.isBanned() == false && !Objects.equals(userRequest.getId(), checkUserEmail.getId())) {
        result.setMessage("Error: Email sudah digunakan!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      Role role = roleRepository.findById(userRequest.getRole()).orElse(null);
      if (role == null) {
        result.setMessage("Error: Role tidak ditemukan!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      /* Validasi ketika noHp sudah digunakan oleh user lain */
      Integer checkUserNoHp = userRepository.existsByNoHp(userRequest.getNoHp(), userRequest.getId());
      if (checkUserNoHp != null && checkUserNoHp > 0) {
        result.setMessage("Error: Nomor telepon sudah digunakan!");
        result.setSuccess(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity
                .badRequest()
                .body(result);
      }

      String username = userRequest.getEmail().split("@")[0];
      // Variable tampungan username
      String tempUsername = username;
      // Lakukan perulangan jika username sudah ada
      while (userRepository.existsByUsername(username) > 0) {
        Random rnd = new Random();
        String newUsername = tempUsername + rnd.nextInt(100);
        username = newUsername;
      }
      
      if (userRequest.isIsActive() == null) {
        userRequest.setIsActive(userLama.get().isIsActive());
      }

      User user = userLama.get();

      user.setUsername(username);
      user.setNamaLengkap(userRequest.getNamaLengkap());
      user.setEmail(userRequest.getEmail());
      user.setNoHp(userRequest.getNoHp());
      user.setRole(role);
      user.setIsActive(userRequest.isIsActive());
      if (userRequest.isIsActive() == true) {
        user.setVerified(userRequest.isIsActive());
      }

      userRepository.save(user);

      result.setMessage("User berhasil di-update.");
      result.setCode(HttpStatus.OK.value());
    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
      result.setSuccess(false);
      result.setMessage(e.getMessage());
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity.badRequest().body(result);
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
      int response = userRepository.deleteUser(banned, id);
      if (response == 1) {
        result.setCode(HttpStatus.OK.value());
        result.setSuccess(true);
        if (banned == true) {
          result.setMessage("User berhasil di-banned.");
        } else {
          result.setMessage("User berhasil di-unbanned.");
        }
      } else {
        result.setSuccess(false);
        result.setMessage("Id User tidak ditemukan!");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(result);
      }
    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
      result.setSuccess(false);
      result.setMessage(e.getMessage());
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity.badRequest().body(result);
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

  private void sendActivationEmail(String receiver, String password, String tokenVerification) {
    EmailDetails emailDetails = new EmailDetails();
    emailDetails.setSubject("Activate User");
    String namaUser = userRepository.findByEmail(receiver).get().getNamaLengkap();
    Boolean isActive = userRepository.findByEmail(receiver).get().isVerified();

    String url = "";

    String buttonLink = "";
    if (isActive) {
      buttonLink = 
      "        <p style=\"font-size: 20px; font-weight: 300;\">Silahkan anda login dengan menekan tombol dibawah ini.</p>\n" +
      "        <br>" +
      "        <a href=\"" + urlBase + "login\"" + " target=\"_blank\"\n" +
      "        style=\"align-self: center; width: 399px; height: 55px; margin: 35px 0; padding: 10px; color: white; text-align: center; text-decoration: none; font-size: 24px; font-weight: 600; background-color: #0D9CA8; cursor: pointer; border: none; border-radius: 8px;\"\n" +
      "        >Login</a><br><br>\n";
    } else {
      buttonLink =
      "        <p style=\"font-size: 20px; font-weight: 300;\">Silahkan anda verifikasi terlebih dahulu dengan menekan tombol dibawah ini.</p>\n" +
      "        <br>" +
      "        <a href=\"" + urlBase + urlActivation + "?tokenVerification=" + tokenVerification + "\" target=\"_blank\"\n" +
      // "        <a href=\"" + "http://localhost:8880/api/auth/active/?tokenVerification=" + tokenVerification + "\" target=\"_blank\"\n" +
      "        style=\"align-self: center; width: 399px; height: 55px; margin: 35px 0; padding: 10px; color: white; text-align: center; text-decoration: none; font-size: 24px; font-weight: 600; background-color: #0D9CA8; cursor: pointer; border: none; border-radius: 8px;\"\n" +
      "        >Aktivasi Akun</a><br><br>\n";
    }

    String body = 
    "<!DOCTYPE html>\n" +
    "<html lang=\"en\" xmlns:th=\"http://www.w3.org/1999/xhtml\">\n" +
    "<head>\n" +
    "  <meta charset=\"UTF-8\">\n" +
    "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
    "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
    "  <title>Template Email - Sign Up</title>\n" +
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
    "    <div style=\"padding: 57px; align-items: center; justify-content: space-between; text-align: center;\">\n" +
    "      <img src=\"cid:logo\" alt=\"Logo Kawah Edukasi\">\n" +
    "      <p style=\"font-size: 24px; font-weight: 600;\">Halo "+namaUser+",</p>\n" +
    "      <p style=\"font-size: 20px; font-weight: 300;\">Selamat akun Kawah Edukasi anda berhasil dibuat.</p>\n" +
    "      <hr style=\"margin-bottom: 10px; margin-top: 10px;\">\n" +
    "      <div>\n" +
    "        <p style=\"font-size: 20px; font-weight: 300;\">Berikut detail akun anda:</p>\n" +
    "        <p style=\"font-size: 20px; font-weight: 300;\">Email: "+receiver+"</p>\n" +
    "        <p style=\"font-size: 20px; font-weight: 300;\">Password: "+password+" </p>\n" +
    "        <hr style=\"margin-bottom: 10px; margin-top: 10px;\">\n" +
             buttonLink +
    "        <hr style=\"margin-bottom: 10px; margin-top: 10px;\">\n" +
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
    String tokenForgotPassword = jwtUtils.generateTokenForgotPassword(checkUserEmail.getUsername(),jwtExpiredTokenForgotPassword);
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
            "          href="+urlForgotPassword+tokenForgotPassword+" \n" +
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
    String urlendpoint = urlForgotPassword+tokenForgotPassword;
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

    Map<String,Object> tokenDecode = jwtUtils.decode(param.getToken());

    // password validation
//    if(!validator.isPasswordValid(param.getPassword())){
//      result.setMessage("Password must be longer than 8 characters,use at least 1 uppercase letter,spesial characters and not contain spaces!!");
//      result.setCode(400);
//      result.setSuccess(false);
//      return ResponseEntity.badRequest().body(result);
//    }

    long userId = Long.parseLong(tokenDecode.get("userId").toString());
    int resultModel = userRepository.changePassword(encoder.encode(param.getPassword()),userId);
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

class SubUser {
  private Long id;
  private String namaLengkap;
  private String email;
  private String noHp;
  private Role role;
  private Boolean status;

  public SubUser() {}

  public SubUser(Long id, String namaLengkap, String email, String noHp, Role role, Boolean status) {
    this.id = id;
    this.namaLengkap = namaLengkap;
    this.email = email;
    this.noHp = noHp;
    this.role = role;
    this.status = status;
  }

  public Long getId() {
      return id;
  }

  public String getNamaLengkap() {
      return namaLengkap;
  }

  public String getEmail() {
      return email;
  }
  
  public String getNoHp() {
      return noHp;
  }

  public Role getRole() {
      return role;
  }

  public Boolean isStatus() {
      return status;
  }
  
}
