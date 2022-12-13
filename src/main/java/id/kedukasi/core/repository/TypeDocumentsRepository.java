package id.kedukasi.core.repository;


import id.kedukasi.core.models.TypeDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface TypeDocumentsRepository extends JpaRepository<TypeDocuments,Integer> {

    @Transactional
    Optional<TypeDocuments>findBytypeName(String typeName);

    @Transactional
    @Query("select count(*) as jumlah from TypeDocuments as p where lower(p.typeName) = ?1")
    int findtypeName(String typeName);


}
