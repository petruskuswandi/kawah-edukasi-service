package id.kedukasi.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.kedukasi.core.request.*;
import id.kedukasi.core.models.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
public interface UserService {
  ResponseEntity<?> createUser(SignupRequest signUpRequest);
  
  ResponseEntity<?> updateUser(UserRequest UserRequest);

  ResponseEntity<?> refreshToken(TokenRefreshRequest tokenRefreshRequest);

  ResponseEntity<?> signIn(LoginRequest loginRequest, String uri);

  ResponseEntity<?> updateProfilePictureBlob(long id, MultipartFile profilePicture, String uri);

  ResponseEntity<?> updateProfilePictureFolder(long id, MultipartFile profilePicture, String uri);

  ResponseEntity<?> deleteUser(boolean banned, long id, String uri);

  ResponseEntity<?> changePassword(long id, String password, String uri);

  ResponseEntity<?> forgotPassword(String email, String uri) throws IOException;

  ResponseEntity<?> changePasswordForgot(ChangePasswordRequest param) throws JsonProcessingException;

  Result signOut(long id, String uri);

  Result active(long id, String tokenVerification, String uri);

  Result getAllUser(String uri);

  Result getUserData(int page, String uri);

  Result getUserById(long id, String uri);
}
