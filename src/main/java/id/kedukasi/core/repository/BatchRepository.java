package id.kedukasi.core.repository;

import id.kedukasi.core.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    @Transactional
    Optional<Batch> findByBatchname(String username);
    @Modifying
    @Transactional
    @Query("update Batch u set u.banned = ?1, u.banned_time = CURRENT_TIMESTAMP where u.id = ?2")
    int deleteBatch(boolean banned, Long id);

    @Transactional
    @Query(value = "SELECT count(*) FROM batches WHERE banned = false", nativeQuery = true)
    int bannedfalse();
    @Transactional
    @Query(
            value = "SELECT * FROM batches WHERE banned = false AND"+
                    "(:batchname IS NULL OR LOWER(batchname) LIKE %:batchname%) "+
                    "ORDER BY id LIMIT :limit OFFSET :page",
            nativeQuery = true
    )
    List<Batch> findBatchData(@Param("batchname") String search,
                              @Param("limit") int limit,
                              @Param("page") int page);
}
