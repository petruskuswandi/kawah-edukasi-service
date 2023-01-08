package id.kedukasi.core.repository;


import id.kedukasi.core.models.Educations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Educations,Integer> {
    Educations findByName(String name);
}
