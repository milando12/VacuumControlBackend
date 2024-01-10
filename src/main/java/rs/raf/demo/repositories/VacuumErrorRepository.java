package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.VacuumError;

import java.util.List;

@Repository
public interface VacuumErrorRepository extends JpaRepository<VacuumError, Long> {
    List<VacuumError> findAllByVacuum_Id(Long id);
}
