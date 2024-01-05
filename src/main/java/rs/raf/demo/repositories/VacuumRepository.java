package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;

@Repository
public interface VacuumRepository  extends JpaRepository<Vacuum, Long> {
}
