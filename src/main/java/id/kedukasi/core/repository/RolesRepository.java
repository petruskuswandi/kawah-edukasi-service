package id.kedukasi.core.repository;

import id.kedukasi.core.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository  extends JpaRepository<Role, Long> {
}
