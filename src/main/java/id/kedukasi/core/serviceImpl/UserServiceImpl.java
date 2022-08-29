package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumRole;
import id.kedukasi.core.models.JwtResponse;
import id.kedukasi.core.models.LoginRequest;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.Role;
import id.kedukasi.core.models.User;
import id.kedukasi.core.repository.RoleRepository;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.models.SignupRequest;
import id.kedukasi.core.service.UserService;
import id.kedukasi.core.utils.JwtUtils;
import id.kedukasi.core.utils.StringUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  private Result result;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public Result getAllUser(String uri) {
    result = new Result();
    try {
      Map items = new HashMap();
      items.put("items", userRepository.findAll());
      result.setData(items);
      result.setCode(String.valueOf(HttpStatus.OK.value()));
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
      result.setCode(String.valueOf(HttpStatus.OK.value()));
    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
    }

    return result;
  }

  @Override
  public ResponseEntity<?> createUser(SignupRequest signUpRequest) {
    result = new Result();

    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      result.setMessage("Error: Username is already taken!");
      result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
      return ResponseEntity
          .badRequest()
          .body(result);
    }
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      result.setMessage("Error: Email is already in use!");
      result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
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
    result.setCode(String.valueOf(HttpStatus.OK.value()));
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity signIn(LoginRequest loginRequest, String uri) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    userRepository.setIsLogin(true, userDetails.getId());
    return ResponseEntity.ok(new JwtResponse(jwt,
        userDetails.getId(),
        userDetails.getUsername(),
        userDetails.getEmail(),
        roles));
  }

  @Override
  public Result signOut(long id, String uri) {
    result = new Result();
    try {
      int status = userRepository.setIsLogin(false, id);
      if (status == 1) {
        result.setMessage("success");
        result.setCode(String.valueOf(HttpStatus.OK.value()));
      } else {
        result.setMessage("failed");
        result.setSuccess(false);
        result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
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
        result.setCode(String.valueOf(HttpStatus.OK.value()));
      } else {
        result.setMessage("failed");
        result.setSuccess(false);
        result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
      }

    } catch (Exception e) {
      logger.error(stringUtil.getError(e));
    }

    return result;
  }
}
