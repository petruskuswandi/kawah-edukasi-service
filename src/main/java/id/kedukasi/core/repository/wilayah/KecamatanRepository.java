package id.kedukasi.core.repository.wilayah;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.kedukasi.core.models.wilayah.Kecamatan;

@Repository
public interface KecamatanRepository extends JpaRepository<Kecamatan, Long>{
    
}
