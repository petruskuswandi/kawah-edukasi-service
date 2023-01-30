package id.kedukasi.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import id.kedukasi.core.models.Syillabus;


@Repository
public interface SyillabusRepository extends JpaRepository<Syillabus, Long>{
    
    @Transactional
    Optional<Syillabus> findBySyillabusName(String syillabusName);

    @Transactional
    @Query(value = "select * from syillabus where is_deleted = ?1 and syllabus_name = ?2", nativeQuery = true)
    Optional<Syillabus> findDellete(boolean dellete, String syillabus);

    @Transactional
    @Query("select count(*) as jumlah from Syillabus as s where lower(s.syillabusName) = ?1")
    int findSyillabusName(String syillabusName);

    @Transactional
    @Query(value = "select * from syillabus WHERE (:syllabus_name is null or syllabus_name like %:syllabus_name%)" +
            "order by id ASC limit :limit offset :offset", nativeQuery = true)
    List<Syillabus> findAllSyillabus(@Param("syllabus_name") String search,
    @Param("limit") long limit,
    @Param("offset") long offset);

    @Query(value = "select count(*) from syillabus", nativeQuery = true)
    Long countSyillabusData();

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
