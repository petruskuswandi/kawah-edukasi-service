package id.kedukasi.core.repository.wilayah;

import id.kedukasi.core.models.wilayah.MasterKota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KotaRepository extends JpaRepository<MasterKota,Long> {

}
