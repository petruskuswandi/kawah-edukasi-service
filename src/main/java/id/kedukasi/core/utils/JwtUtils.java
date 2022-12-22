package id.kedukasi.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.kedukasi.core.models.User;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.serviceImpl.UserDetailsImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;
@Component
public class JwtUtils {

  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
  @Value("${kedukasi.app.jwtSecret}")
  private String jwtSecret;
  @Value("${kedukasi.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Autowired
  UserRepository userRepository;

  public String generateJwtToken(Authentication authentication, Date dateNow, Date dateExpired) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    return Jwts.builder()
            .setSubject((userPrincipal.getUsername()))
            .setIssuedAt(dateNow)
            .setExpiration(dateExpired)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
  }

  public String generateJwtActiveUser(Authentication authentication, Date dateNow, Date dateExpired) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    return Jwts.builder()
            .setSubject("kedukasi")
            .setIssuedAt(dateNow)
            .setExpiration(dateExpired)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .setHeaderParam("id", userPrincipal.getId())
            .setHeaderParam("email", userPrincipal.getEmail())
            .compact();
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
  }

  public String generateTokenForgotPassword(String username,long expiredDate) {
    User user = userRepository.findByUsername(username).get();
    Map<String,Object> claims = new HashMap<>();
    claims.put("userId",user.getId());
    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + expiredDate)).signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken, HttpServletRequest request) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
      request.setAttribute("expired",e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  public boolean validateJwtToken(String token) throws JsonProcessingException {
    try{
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  public Map<String,Object> decode(String token) throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    String[] tokenSplit = token.split("\\.");
    Base64.Decoder decoder = Base64.getUrlDecoder();
    String header = new String(decoder.decode(tokenSplit[0]));
    String payload = new String(decoder.decode(tokenSplit[1]));
    Map<String,Object> mapPayload = om.readValue(payload, new TypeReference<Map<String, Object>>() {});
    return mapPayload;
  }
}
