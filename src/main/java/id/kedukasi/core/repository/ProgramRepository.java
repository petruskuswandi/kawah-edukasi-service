package id.kedukasi.core.repository;

import id.kedukasi.core.models.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface ProgramRepository extends JpaRepository<Program,Long> {
    @Transactional
    Optional<Program> findByprogramName(String program_name);

    @Transactional
    @Query("select count(*) as jumlah from Program as p where lower(p.programName) = ?1")
    int findProgramname(String program_name);
}
