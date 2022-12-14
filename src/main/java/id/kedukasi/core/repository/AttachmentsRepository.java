package id.kedukasi.core.repository;

import id.kedukasi.core.models.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AttachmentsRepository extends JpaRepository <Attachments, Long>{

    @Transactional
    Optional<Attachments> findByattachmentsName(String filename);

    @Transactional
    @Query("select count(*) as jumlah from Attachments as d where lower(d.attachmentsName) = ?1")
    int findAttachmentsname(String filename);

}
