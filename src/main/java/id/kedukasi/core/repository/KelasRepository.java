package id.kedukasi.core.repository;

import id.kedukasi.core.models.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface KelasRepository extends JpaRepository<Kelas,Long> {

    @Transactional
    Optional<Kelas> findByClassname(String username);

    @Transactional
    Kelas findById(long id);

    @Modifying
    @Transactional
    @Query("update Kelas u set u.banned = ?1, u.banned_time = CURRENT_TIMESTAMP where u.id = ?2")
    int deleteKelas(boolean banned, Long id);
}
