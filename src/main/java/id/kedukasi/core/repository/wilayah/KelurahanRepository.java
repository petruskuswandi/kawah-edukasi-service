package id.kedukasi.core.repository.wilayah;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.kedukasi.core.models.wilayah.Kelurahan;

@Repository
public interface KelurahanRepository extends JpaRepository<Kelurahan, Long>{
    
}
