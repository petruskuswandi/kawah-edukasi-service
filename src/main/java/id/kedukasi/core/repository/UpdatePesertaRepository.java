package id.kedukasi.core.repository;

import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface UpdatePesertaRepository extends JpaRepository<Peserta,Long>  {

    //get by id
    @Transactional
    @Query("select p from Peserta p where p.id=?1")
    Peserta getPesertaById(Long id);

    @Modifying
    @Transactional
    @Query("update Peserta u set u.status = ?1, u.updated_time = CURRENT_TIMESTAMP where u.id = ?2")
    int statusPeserta(Status status, Long id);

    @Transactional
    @Query(value = "select p.* from Peserta p where p.status_id = :statusPeserta and p.banned = :banned and " +
            "(:namaPeserta is null or p.nama_peserta like %:namaPeserta%) " +
            "order by p.id ASC limit :limit offset :offset",nativeQuery = true)
    List<Peserta> getAllStatus(@Param("statusPeserta") int statusPeserta, @Param("banned") boolean banned,
                               @Param("namaPeserta") String namaPeserta, @Param("limit")long limit,
                               @Param("offset") long offset);
}
