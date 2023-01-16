package id.kedukasi.core.repository;

import id.kedukasi.core.models.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Transactional
  Optional<User> findByUsername(String username);

  @Transactional
  Optional<User> findByEmail(String email);

  @Transactional
  @Query("select count(*) from User u where u.username = ?1 and u.banned != true")
  Integer existsByUsername(String username);

  @Transactional
  @Query("select count(*) from User u where u.email = ?1 and u.banned != true")
  Integer existsByEmail(String email);

  /* Query untuk cek apakah terdapat noHp yang sama pada user lain */
  /* For create */
  @Transactional
  @Query("select count(*) from User u where u.noHp = ?1 and u.banned != true")
  Integer existsByNoHp(String noHp);

  /* For update */
  @Transactional
  @Query("select count(*) from User u where u.noHp = ?1 and u.id != ?2 and u.banned != true")
  Integer existsByNoHp(String noHp, Long id);

  /* Query untuk cek apakah token sudah ada pada user lain */
  @Transactional
  @Query("select count(*) from User u where u.tokenVerification = ?1 and u.banned != true")
  Integer existsByToken(String token);

  @Transactional
  @Query("select u.isVerified from User u where u.tokenVerification = ?1 and u.banned != true")
  boolean isUserVerified(String token);

  @Transactional
  @Query(
    value = "SELECT * FROM users WHERE banned = false AND "+
            "(:namaLengkap IS NULL OR LOWER(nama_lengkap) LIKE %:namaLengkap%) "+
            "ORDER BY id LIMIT :limit OFFSET :limit * (:page - 1)", 
    nativeQuery = true
  )
  List<User> findUserData(@Param("namaLengkap") String search, 
                          @Param("limit") int limit, 
                          @Param("page") int page);

  @Transactional
  @Query(
    value = "SELECT count(*) FROM users WHERE banned = false",
    nativeQuery = true
  )
  Integer totalUnbannedUser();

  @Transactional
  User findById(long id);

  @Modifying
  @Transactional
  @Query("update User u set u.isLogin = ?1 where u.id = ?2")
  int setIsLogin(boolean isLogin, Long id);

  @Modifying
  @Transactional
  @Query("update User u set u.isActive = ?1 where u.tokenVerification = ?2")
  int setIsActive(boolean isAcvtive, String tokenVerification);

  @Modifying
  @Transactional
  @Query("update User u set u.isVerified = ?1 where u.tokenVerification = ?2")
  int setIsVerified(boolean isVerified, String tokenVerification);

  @Modifying
  @Transactional
  @Query("update User u set u.profilePicture = ?1 where u.id = ?2 ")
  int updateProfilePicture(byte[] image, Long id);

  @Modifying
  @Transactional
  @Query("update User u set u.banned = ?1, u.banned_time = CURRENT_TIMESTAMP where u.id = ?2")
  int deleteUser(boolean banned, Long id);
  
  @Modifying
  @Transactional
  @Query("update User u set u.password = ?1, u.updated_time = CURRENT_TIMESTAMP where u.id = ?2")
  int changePassword(String password, Long id);

  @Modifying
  @Transactional
  @Query("update User u set u.profilePicturePath = ?1, u.updated_time = CURRENT_TIMESTAMP where u.id = ?2")
  int setProfilePicturePath(String profilePicturePath, Long id);
}
