package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.VacuumError;

@Repository
public interface VacuumErrorRepository extends JpaRepository<VacuumError, Long> {
}
