package id.kedukasi.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import id.kedukasi.core.models.FAQ;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Integer> {

    /* Native query select untuk mendapatkan data FAQ dengan id tertentu */
    @Transactional
    @Query(
        value = "SELECT * FROM faqs WHERE id = :id",
        nativeQuery = true
    )
    Optional<FAQ> selectFAQById(@Param("id") int id);
    
    /* Native query select dengan limit dan offset (page) untuk mendapatkan data FAQ */
    @Transactional
    @Query(
        value = "SELECT * FROM faqs ORDER BY id LIMIT :limit OFFSET :limit * (:page - 1)",
        nativeQuery = true
    )
    List<FAQ> findFAQData(@Param("limit") int limit, @Param("page") int page);

}
