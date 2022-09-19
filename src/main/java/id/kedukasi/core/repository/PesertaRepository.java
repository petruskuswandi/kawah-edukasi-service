package id.kedukasi.core.repository;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.enums.EnumStatusTes;
import id.kedukasi.core.models.Peserta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PesertaRepository extends JpaRepository<Peserta,Long> {
    @Transactional
    Peserta findById(long id);

    @Transactional
    Optional<Peserta> findByNamaPeserta(String username);

    @Transactional
    Optional<Peserta> findByEmail(String email);

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

    @Modifying
    @Transactional
    @Query("update Peserta u set u.uploadImage = ?1 where u.id = ?2 ")
    int updateUploadImage(byte[] image, Long id);

    @Modifying
    @Transactional
    @Query("update Peserta u set u.uploadImagePath = ?1, u.updated_time = CURRENT_TIMESTAMP where u.id = ?2")
    int setUploadImagePath(String uploadImagePath, Long id);

    @Transactional
    @Query("SELECT p FROM Peserta p WHERE p.namaPeserta LIKE %?1% AND p.statusPeserta = ?2")
    List<Peserta> search(String keyword, EnumStatusPeserta statusPeserta);
}
