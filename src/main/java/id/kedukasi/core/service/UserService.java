package id.kedukasi.core.service;

import id.kedukasi.core.models.LoginRequest;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.SignupRequest;
import org.springframework.http.ResponseEntity;


public interface UserService {

  ResponseEntity<?> createUser(SignupRequest signUpRequest);

  ResponseEntity<?> signIn(LoginRequest loginRequest, String uri);

  Result signOut(long id, String uri);

  Result active(long id, String tokenVerification, String uri);

  Result getAllUser(String uri);

  Result getUserById(long id, String uri);
}
