package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;

import java.util.List;

@Repository
public interface VacuumRepository  extends JpaRepository<Vacuum, Long> {
//    List<Vacuum> findAllByActiveIsTrueAndUserId(Long userId);
    List<Vacuum> findAllByActiveIsTrueAndUser_Id(Long userId);
}
