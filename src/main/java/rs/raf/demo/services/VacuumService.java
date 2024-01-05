package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumRepository;
import rs.raf.demo.requests.VacuumRequest;

import java.util.List;


@Service
public class VacuumService {
    private VacuumRepository vacuumRepository;
    private UserRepository userRepository;

    @Autowired
    public VacuumService(UserRepository userRepository, VacuumRepository vacuumRepository) {
        this.vacuumRepository = vacuumRepository;
        this.userRepository = userRepository;
    }

    public Vacuum add(VacuumRequest vacuumRequest) {
        System.out.println(vacuumRequest.getName());
        System.out.println(vacuumRepository.findAll());

        Vacuum vacuum = new Vacuum();
        vacuum.setName(vacuumRequest.getName());
        vacuum.setStatus(Status.STOPPED);
        vacuum.setActive(true);
        vacuum.setCreationTime(java.time.LocalDate.now());
        vacuum.setUser(this.getUser());

        return this.vacuumRepository.save(vacuum);
    }

    // Test
    public List<Vacuum> findAll() {
        return this.vacuumRepository.findAll();
    }

//  Complementary methods:

    private User getUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("EMAIL:\t"+email);
        return this.userRepository.findByEmail(email);
    }
}
