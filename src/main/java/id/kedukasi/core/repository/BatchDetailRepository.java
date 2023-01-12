package id.kedukasi.core.repository;

import id.kedukasi.core.models.BatchDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface BatchDetailRepository extends JpaRepository<BatchDetail, Long> {

    @Transactional
    @Query(value = "select b.class_id from batch_class b where b.batch_id = :batchId",nativeQuery = true)
    List<Long> getAllClassByBatch(@Param("batchId") long batchId);
}
