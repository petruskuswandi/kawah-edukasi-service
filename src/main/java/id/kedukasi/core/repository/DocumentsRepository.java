package id.kedukasi.core.repository;

import java.util.List;

import id.kedukasi.core.models.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Integer> {

    List<Documents> findAllByUserId(Long id);
}
