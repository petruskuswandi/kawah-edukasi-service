package id.kedukasi.core.repository;

import id.kedukasi.core.models.Batch;
import id.kedukasi.core.models.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface KelasRepository extends JpaRepository<Kelas,Long> {

    @Transactional
    Optional<Kelas> findByClassname(String username);

    @Modifying
    @Transactional
    @Query("update Kelas u set u.banned = ?1, u.banned_time = CURRENT_TIMESTAMP where u.id = ?2")
    int deleteKelas(boolean banned, Long id);


//    @Transactional
//    @Query("select b from Batch b where b.classname = :idKelas")
//    List<Batch> getAllBatch(@Param("idKelas") Kelas idKelas);
}
