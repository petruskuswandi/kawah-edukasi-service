package id.kedukasi.core.repository;


import id.kedukasi.core.models.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education,Integer> {
    Education findByName(String name);
}
