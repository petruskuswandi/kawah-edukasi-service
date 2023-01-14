package id.kedukasi.core.repository;

import java.util.List;

import id.kedukasi.core.models.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Integer> {

    @Query("SELECT d FROM Documents d WHERE d.user.id = :userId AND d.banned = false ")
    List<Documents> findAllUndeletedByUserId(@Param("userId") Long userId);
}
