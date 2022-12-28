package id.kedukasi.core.repository;

import id.kedukasi.core.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

}
