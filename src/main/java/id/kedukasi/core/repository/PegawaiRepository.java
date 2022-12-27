package id.kedukasi.core.repository;


import id.kedukasi.core.models.Pegawai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PegawaiRepository extends JpaRepository<Pegawai, Long> {
    
}
