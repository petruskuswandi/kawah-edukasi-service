package id.kedukasi.core.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import id.kedukasi.core.models.Mentor;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long>{

  @Transactional
  Mentor findById(long id);

  @Modifying
  @Transactional
  @Query("update Mentor m set m.banned = ?1, m.banned_time = CURRENT_TIMESTAMP where m.id = ?2")
  int deleteMentor(boolean banned, Long id);

  @Transactional
  @Query(value = "SELECT count(*) FROM mentors WHERE DATE_PART('year', created_time) = :year", nativeQuery = true)
  int jumlahmentor(@Param("year") int year);

  @Transactional
  @Query(value = "SELECT count(kode) FROM mentors WHERE kode = :kode", nativeQuery = true)
  int cekkode(@Param("kode") String kode);

  @Transactional
  @Query(value = "SELECT count(*) FROM mentors WHERE banned = false", nativeQuery = true)
  int bannedfalse();

  @Transactional
  @Query(value = "SELECT * FROM mentors WHERE banned = ?1 AND email = ?2", nativeQuery = true)
  Optional<Mentor> findByemail(boolean banned,String email);
  @Transactional
  @Query(value = "SELECT * FROM mentors WHERE banned = ?1 AND email = ?2 AND id != ?3", nativeQuery = true)
  Optional<Mentor> findByemailUpdate(boolean banned, String email, Long id);

  @Transactional
  @Query(value = "SELECT kode FROM mentors WHERE id = :id", nativeQuery = true)
  String ambilkode(@Param("id") Long id);

  @Transactional
  @Query(
          value = "SELECT * FROM mentors WHERE banned = false AND " +
                  "(:namamentor IS NULL OR LOWER(namamentor) LIKE %:namamentor%)" +
                  "ORDER BY id LIMIT :limit OFFSET :page",
          nativeQuery = true
  )
  List<Mentor> findMentorData(@Param("namamentor") String search,
                            @Param("limit") int limit,
                            @Param("page") int page);
}
