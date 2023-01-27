package id.kedukasi.core.repository;

import id.kedukasi.core.models.Kelas;
import org.springframework.data.domain.PageRequest;
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

    @Transactional
    @Query(value = "select * from classes where banned = ?1 and classname = ?2",  nativeQuery = true)
    Optional<Kelas> findBanned(boolean banned, String clasName);


    @Modifying
    @Transactional
    @Query("update Kelas u set u.banned = ?1, u.banned_time = CURRENT_TIMESTAMP where u.id = ?2")
    int deleteKelas(boolean banned, Long id);

    @Transactional
    @Query(
            value = "select * from classes WHERE banned = :banned and "+
                    "(:classname is null or classname like %:classname%) "+
                    "order by id ASC limit :limit offset :offset",
            nativeQuery = true
    )
    List<Kelas> findKelasData(@Param("classname") String search,
                              @Param("banned") boolean banned,
                              @Param("limit") long limit,
                              @Param("offset") long offset);

    @Query(value = "select count(*) from classes where banned = false", nativeQuery = true)
    Long countKelasData(@Param("banned") boolean banned);


//    @Transactional
//    /    @Query("select b from Batch b where b.classname = :idKelas ")\
//    List<Batch> getAllBatch(@Param("idKelas") Kelas idKelas);
}
