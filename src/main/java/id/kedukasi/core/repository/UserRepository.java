package id.kedukasi.core.repository;

import id.kedukasi.core.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Transactional
  Optional<User> findByUsername(String username);

  @Transactional
  Optional<User> findByEmail(String email);

  @Transactional
  Boolean existsByUsername(String username);

  @Transactional
  Boolean existsByEmail(String email);

  @Transactional
  User findById(long id);

  @Modifying
  @Transactional
  @Query("update User u set u.isLogin = ?1 where u.id = ?2")
  int setIsLogin(boolean isLogin, Long id);

  @Modifying
  @Transactional
  @Query("update User u set u.isActive = ?1 where u.id = ?2 and u.tokenVerification = ?3")
  int setIsActive(boolean isAcvtive, Long id, String tokenVerification);

  @Modifying
  @Transactional
  @Query("update User u set u.profilePicture = ?1 where u.id = ?2 ")
  int updateProfilePicture(byte[] image, Long id);
}
