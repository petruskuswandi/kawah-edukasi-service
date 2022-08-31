package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumRole;
import id.kedukasi.core.exception.TokenRefreshException;
import id.kedukasi.core.models.JwtResponse;
import id.kedukasi.core.models.LoginRequest;
import id.kedukasi.core.models.RefreshToken;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.Role;
import id.kedukasi.core.models.User;
import id.kedukasi.core.repository.RoleRepository;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.models.SignupRequest;
import id.kedukasi.core.models.TokenRefreshRequest;
import id.kedukasi.core.models.TokenRefreshResponse;
import id.kedukasi.core.service.UserService;
import id.kedukasi.core.utils.JwtUtils;
import id.kedukasi.core.utils.StringUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

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
      Map items = new HashMap();
      items.put("items", userRepository.findById(id));
      result.setData(items);
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

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();
    if (strRoles == null) {
      Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            break;
          case "mod":
            Role modRole = roleRepository.findByName(EnumRole.ROLE_MODERATOR)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);
            break;
          default:
            Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }
    user.setRoles(roles);
    user.setIsActive(false);
    user.setIsLogin(false);
    userRepository.save(user);

    result.setMessage("User registered successfully!");
    result.setCode(HttpStatus.OK.value());
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity signIn(LoginRequest loginRequest, String uri) {
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
      List<String> roles = userDetails.getAuthorities().stream()
          .map(item -> item.getAuthority())
          .collect(Collectors.toList());

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

      result.setSuccess(true);
      result.setMessage("success");
      result.setData(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(),
          userDetails.getEmail(), roles, dateExpired.getTime()));
    }else{
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
  public Result active(long id, String tokenVerification,
      String uri
  ) {
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

    if (userRepository.existsByEmail(user.getEmail())) {
      result.setMessage("Error: Email is already in use!");
      result.setCode(HttpStatus.BAD_REQUEST.value());
      return ResponseEntity
          .badRequest()
          .body(result);
    }

    return ResponseEntity.ok(result);
  }
}
