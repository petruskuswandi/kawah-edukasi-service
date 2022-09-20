package id.kedukasi.core.repository.wilayah;

import id.kedukasi.core.models.wilayah.MasterKelurahan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KelurahanRepository extends JpaRepository<MasterKelurahan,Integer> {
}
