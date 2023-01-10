package id.kedukasi.core.repository;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.enums.EnumStatusTes;
import id.kedukasi.core.models.Peserta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PesertaRepository extends JpaRepository<Peserta,Long> {
    @Transactional
    Optional<Peserta> findByNamaPeserta(String username);

    @Transactional
    @Query("SELECT p FROM Peserta p WHERE p.namaPeserta LIKE %?1% AND p.statusPeserta = 'CALON' AND p.banned=false ")
    List<Peserta> getCalonPeserta(String name);

    @Transactional
    Optional<Peserta> findByEmail(String email);

    @Transactional
    Optional<Peserta> findByNoHp(String noHp);

    @Transactional
    Optional<Peserta> findByNomorKtp(String nomorKtp);

    @Modifying
    @Transactional
    @Query("update Peserta u set u.banned = ?1, u.banned_time = CURRENT_TIMESTAMP where u.id = ?2")
    int deletePeserta(boolean banned, Long id);

    @Modifying
    @Transactional
    @Query("update Peserta u set u.statusPeserta = ?1, u.updated_time = CURRENT_TIMESTAMP where u.id = ?2")
    int statusPeserta(EnumStatusPeserta EnumStatusPeserta, Long id);

    @Modifying
    @Transactional
    @Query("update Peserta u set u.statusTes = ?1, u.updated_time = CURRENT_TIMESTAMP where u.id = ?2")
    void statusTes(EnumStatusTes enumStatusTes, long id);

//    @Modifying
//    @Transactional
//    @Query("update Peserta u set u.uploadImage = ?1 where u.id = ?2 ")
//    int updateUploadImage(byte[] image, Long id);
//
    @Modifying
    @Transactional
    @Query("update Peserta u set u.uploadImagePath = ?1, u.updated_time = CURRENT_TIMESTAMP where u.id = ?2")
    int setUploadImagePath(String uploadImagePath, Long id);

    @Transactional
    @Query("SELECT p FROM Peserta p WHERE p.namaPeserta LIKE %?1% AND p.statusPeserta = ?2 AND p.banned = false")
    List<Peserta> search(String keyword, EnumStatusPeserta statusPeserta);

    @Transactional
    @Query("select max(p.id) from Peserta p")
    int getPesertaMaxId();

    @Transactional
    @Query("select p from Peserta p where p.id=?1")
    Peserta getPesertaById(Long id);

    /*
        Feature In JPA getAll
        Search,Pagination
        Parameter Required: limit and offset
        Parameter Optional: search
        * if no parameter, default value limit=10,offset=0,search=""
     */
    @Transactional
    @Query(value = "select p.* from Peserta p where p.status_peserta = :statusPeserta and p.banned = :banned and " +
            "(:namaPeserta is null or p.nama_peserta like %:namaPeserta%) " +
            "order by p.id ASC limit :limit offset :offset",nativeQuery = true)
    List<Peserta> getAllPagination(@Param("statusPeserta") String statusPeserta,@Param("banned") boolean banned,
                         @Param("namaPeserta") String namaPeserta, @Param("limit")long limit,
                         @Param("offset") long offset);

    @Transactional
    @Query(value = "select count(p.*) from Peserta p where p.status_peserta = :statusPeserta and p.banned = false",nativeQuery = true)
    long getCountByStatus(@Param("statusPeserta") String statusPeserta);
}
