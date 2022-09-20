package id.kedukasi.core.repository.wilayah;

import id.kedukasi.core.models.wilayah.MasterKecamatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KecamatanRepository extends JpaRepository<MasterKecamatan,Integer> {
}
