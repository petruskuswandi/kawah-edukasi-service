package id.kedukasi.core.repository;

import id.kedukasi.core.models.BatchDetail;
import id.kedukasi.core.models.Peserta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface BatchDetailRepository extends JpaRepository<BatchDetail, Long> {

    @Transactional
    @Query("select bd from BatchDetail bd where bd.id=?1")
    BatchDetail getBatchDetailById(Long id);
}
