package id.kedukasi.core.repository;

import java.util.List;
import java.util.Optional;

import id.kedukasi.core.models.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Integer> {

    List<Documents> findAllByUserId(Long id);
}
