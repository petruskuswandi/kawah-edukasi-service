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

}
