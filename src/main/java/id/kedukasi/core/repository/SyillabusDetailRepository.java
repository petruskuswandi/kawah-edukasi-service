package id.kedukasi.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import id.kedukasi.core.models.SyillabusDetail;

@Repository
public interface SyillabusDetailRepository extends JpaRepository<SyillabusDetail, Long>{
    
    @Transactional
    @Query("select sd from SyillabusDetail sd where sd.id=?1")
    SyillabusDetail getSyillabusDetailById(Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from syillabus_detail_syillabus where syillabus_detail_id=:id",nativeQuery = true)
    void deletesyillabusDetailList(Long id);


}
