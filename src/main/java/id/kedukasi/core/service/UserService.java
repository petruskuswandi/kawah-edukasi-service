package id.kedukasi.core.service;

import id.kedukasi.core.models.LoginRequest;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.SignupRequest;
import id.kedukasi.core.models.TokenRefreshRequest;
import id.kedukasi.core.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  ResponseEntity<?> createUser(SignupRequest signUpRequest);

  ResponseEntity<?> updateUser(User user);

  ResponseEntity<?> refreshToken(TokenRefreshRequest tokenRefreshRequest);

  ResponseEntity<?> signIn(LoginRequest loginRequest, String uri);

  ResponseEntity<?> updateProfilePicture(long id, MultipartFile profilePicture, String uri);

  Result signOut(long id, String uri);

  Result active(long id, String tokenVerification, String uri);

  Result getAllUser(String uri);

  Result getUserById(long id, String uri);
}
