package id.kedukasi.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import id.kedukasi.core.models.Syillabus;


@Repository
public interface SyillabusRepository extends JpaRepository<Syillabus, Long>{
    
    @Transactional
    Optional<Syillabus> findBySyillabusName(String syillabusName);

    @Transactional
    @Query("select count(*) as jumlah from Syillabus as s where lower(s.syillabusName) = ?1")
    int findSyillabusName(String syillabusName);

    // @Transactional
    // @Query(value = "select * from Syillabus where is_deleted = true" ,nativeQuery = true)
    // Optional<Syillabus> findSyillabusById(Long Id);

    @Transactional
    @Modifying
    @Query(value = "delete from syillabus_attachments where syillabus_id=:id",nativeQuery = true)
    void deletesyillabusAttachmentList(Long id);

    // @Transactional
    // @Modifying
    // @Query(value = "select * from attachments where id=:id",nativeQuery = true)
    // void findAtachmentId(List<Long> list);


}
