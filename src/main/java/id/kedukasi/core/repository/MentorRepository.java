package id.kedukasi.core.repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import id.kedukasi.core.models.Mentor;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long>{


  @Transactional
  Optional<Mentor> findByKode(String kode);

  // @Transactional
  // Optional<Mentor> findBykode_mentor(String kode_mentor);

  @Transactional
  Mentor findById(long id);

  @Modifying
  @Transactional
  @Query("update Mentor m set m.foto = ?1 where m.id = ?2 ")
  int updateFoto(byte[] image, Long id);

  @Modifying
  @Transactional
  @Query("update Mentor m set m.cv = ?1 where m.id = ?2 ")
  int updateCv(byte[] file, Long id);

  @Modifying
  @Transactional
  @Query("update Mentor m set m.banned = ?1, m.banned_time = CURRENT_TIMESTAMP where m.id = ?2")
  int deleteMentor(boolean banned, Long id);
}
