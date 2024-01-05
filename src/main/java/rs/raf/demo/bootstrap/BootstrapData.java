package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.repositories.*;


@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VacuumRepository vacuumRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder, VacuumRepository vacuumRepository) {
        this.userRepository = userRepository;
        this.vacuumRepository = vacuumRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

//      USERS:

        User admin = new User();
        admin.setName("Admin");
        admin.setSurname("Admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(this.passwordEncoder.encode("admin"));
        admin.setPermissions("can_create_users,can_read_users,can_update_users,can_delete_users,can_search_vacuum,can_start_vacuum,can_stop_vacuum,can_discharge_vacuum,can_add_vacuum,can_remove_vacuum");
        this.userRepository.save(admin);
//        eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6WyJjYW5fY3JlYXRlX3VzZXJzIiwiY2FuX3JlYWRfdXNlcnMiLCJjYW5fdXBkYXRlX3VzZXJzIiwiY2FuX2RlbGV0ZV91c2VycyJdLCJleHAiOjE3MDQ0MDg1MzEsImlhdCI6MTcwNDM3MjUzMX0.Qnw4Fc67urhl0cWhwGcrRf2zQRVHzINDX0ekHdJqScdTBV2gfx8_uWlQvSYGuQY3IqSwKADq0DBe8I22DkPygg

        User c = new User();
        c.setName("cu");
        c.setSurname("c");
        c.setEmail("c@gmail.com");
        c.setPassword(this.passwordEncoder.encode("cu"));
        c.setPermissions("can_create_users");
        this.userRepository.save(c);

        User r = new User();
        r.setName("ru");
        r.setSurname("r");
        r.setEmail("r@gmail.com");
        r.setPassword(this.passwordEncoder.encode("ru"));
        r.setPermissions("can_read_users");
        this.userRepository.save(r);

        User n = new User();
        n.setName("n");
        n.setSurname("n");
        n.setEmail("n@gmail.com");
        n.setPassword(this.passwordEncoder.encode("n"));
        this.userRepository.save(n);

//      VACUUMS:

        Vacuum v1 = new Vacuum();
        v1.setName("Vacuum 1");
        v1.setStatus(Status.STOPPED);
        v1.setActive(true);
        v1.setCreationTime(java.time.LocalDate.now());
        v1.setUser(admin);
        admin.getVacuums().add(v1);
        this.vacuumRepository.save(v1);

        Vacuum v2 = new Vacuum();
        v2.setName("Vacuum 2");
        v2.setStatus(Status.STOPPED);
        v2.setActive(true);
        v2.setCreationTime(java.time.LocalDate.now());
        v2.setUser(c);
        c.getVacuums().add(v2);
        this.vacuumRepository.save(v2);

        System.out.println("Data loaded!");
    }
}
