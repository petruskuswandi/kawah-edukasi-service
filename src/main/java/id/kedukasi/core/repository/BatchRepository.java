package id.kedukasi.core.repository;

import id.kedukasi.core.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    @Transactional
    Optional<Batch> findByBatchname(String username);

    @Modifying
    @Transactional
    @Query("update Batch u set u.banned = ?1, u.banned_time = CURRENT_TIMESTAMP where u.id = ?2")
    int deleteBatch(boolean banned, Long id);
}
