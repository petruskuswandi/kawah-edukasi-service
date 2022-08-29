package id.kedukasi.core.repository;

import id.kedukasi.core.enums.EnumRole;
import id.kedukasi.core.models.Role;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(EnumRole name);
}
