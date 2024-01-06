package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumRepository;
import rs.raf.demo.requests.FilterRequest;
import rs.raf.demo.requests.VacuumRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
        this.getUser().getVacuums().add(vacuum);
//        probably not needed because it is in managed state
        userRepository.save(this.getUser());

        return this.vacuumRepository.save(vacuum);
    }

    public Boolean remove(Long id) {
        Vacuum vacuum = this.vacuumRepository.findById(id).orElse(null);
        if (vacuum == null || vacuum.getStatus()!= Status.STOPPED) {
            return false;
        }
        vacuum.setActive(false);
        this.vacuumRepository.save(vacuum);
        return true;
    }

    public List<Vacuum> findAll() {
        return this.vacuumRepository.findAll();
    }

//  Prettiest solution:     <3
    public List<Vacuum> search(FilterRequest filterRequest) {
        String name = filterRequest.getName();
        List<Status> statuses = getStatuses(filterRequest.getStatuses());
        String dateFrom = filterRequest.getDateFrom();  // yyyy-MM-dd
        String dateTo = filterRequest.getDateTo();
        Long userId= getUser().getId();

        return this.vacuumRepository.findAllByActiveIsTrueAndUser_Id(userId).stream()
                .filter(vacuum -> name == null || vacuum.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(vacuum -> statuses == null || statuses.isEmpty() || statuses.contains(vacuum.getStatus()))
                .filter(vacuum -> dateFrom == null || isValidDate(dateFrom) || vacuum.getCreationTime().isAfter(LocalDate.parse(dateFrom)))
                .filter(vacuum -> dateTo == null || isValidDate(dateTo) || vacuum.getCreationTime().isBefore(LocalDate.parse(dateTo)))
                .collect(Collectors.toList());
    }

    // Test
    public List<Vacuum> findEveryone() {
        return this.vacuumRepository.findAll();
    }


//  Complementary methods:

    private User getUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("EMAIL:\t"+email);
        return this.userRepository.findByEmail(email);
    }

    private List<Status> getStatuses(List<String> statusStrings) {
        if (statusStrings == null || statusStrings.isEmpty()) {
            return null;
        }

        return statusStrings.stream()
                .map(Status::valueOf)
                .collect(Collectors.toList());
    }

//  returns true if date is not in format yyyy-MM-dd
    private boolean isValidDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(dateString);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}
