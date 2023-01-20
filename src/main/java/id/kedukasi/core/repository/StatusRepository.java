package id.kedukasi.core.repository;

import id.kedukasi.core.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status,Integer> {

    @Transactional
    Optional<Status> findBystatusName(String status_name);

    @Transactional
    @Query("select count(*) as jumlah from Status as s where lower(s.statusName) = ?1")
    int findStatusname(String status_name);

    /**
     * Method untuk check kombinasi unique antara column statusName dan flag
     * @param statusName
     * @param flag
     * @param subFlag
     * @return
     */
    @Transactional
    @Query("select count(*) as jumlah from Status as s where lower(s.statusName) = ?1 and lower(s.flag) = ?2 and lower(s.subFlag) = ?3")
    int findStatusNameFlagSubFlag(String statusName, String flag, String subFlag);

    /**
     * Method untuk check kombinasi unique antara column statusName dan flag khusus untuk update
     * @param id
     * @param statusName
     * @param flag
     * @param subFlag
     * @return
     */
    @Transactional
    @Query("select count(*) as jumlah from Status as s where s.id != ?1 and lower(s.statusName) = ?2 and lower(s.flag) = ?3 and lower(s.subFlag) = ?4")
    int findUpdateStatusNameFlagSubFlag(int id, String statusName, String flag, String subFlag);

    /**
     * Method untuk mencari id yang nilai statusName dan flag sama dengan parameter
     * @param statusName
     * @param flag
     * @param subFlag
     * @return
     */
    @Transactional
    @Query("select s.id as identity from Status as s where lower(s.statusName) = ?1 and lower(s.flag) = ?2 and lower(s.subFlag) = ?3")
    int findIdStatusByNameAndFlagAndSubFlag(String statusName, String flag, String subFlag);

    /**
     * Method untuk mendapatkan data status berdasarkan flag dan sub-flag
     * @param flag
     * @param subFlag
     * @return
     */
    @Transactional
    @Query(
        value = "SELECT * FROM status WHERE is_deleted = false AND " +
                "(:flag IS NULL OR lower(flag) LIKE %:flag%) AND " +
                "(:subFlag IS NULL OR lower(sub_flag) LIKE %:subFlag%) " +
                "ORDER BY id LIMIT :limit OFFSET :limit * (:page - 1)",
        nativeQuery = true
    )
    List<Status> getStatus(@Param("flag") String flag, 
                           @Param("subFlag") String subFlag, 
                           @Param("limit") int limit, 
                           @Param("page") int page);

    @Transactional
    @Query(
        value = "SELECT count(*) FROM status WHERE is_deleted = false",
        nativeQuery = true
    )
    Integer totalUndeletedStatus();

    @Modifying
    @Transactional
    @Query("update Status s set s.isDeleted = ?2 where s.id = ?1")
    int deleteStatus(int id, boolean deleted);
}
