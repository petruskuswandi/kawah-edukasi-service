package id.kedukasi.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
