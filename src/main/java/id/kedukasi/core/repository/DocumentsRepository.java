package id.kedukasi.core.repository;

import java.util.Optional;

import id.kedukasi.core.models.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Integer> {

    @Transactional
    Optional<Documents> findBydocumentsName(String file_name);

    @Transactional
    @Query("select count(*) as jumlah from Documents as d where lower(d.documentsName) = ?1")
    int findDocumentsname(String file_name);
}
