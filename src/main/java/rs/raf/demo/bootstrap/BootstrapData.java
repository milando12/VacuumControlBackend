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

// USERS:

        User admin = new User();
        admin.setName("Admin");
        admin.setSurname("Admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(this.passwordEncoder.encode("admin"));
        admin.setPermissions("can_create_users,can_read_users,can_update_users,can_delete_users,can_search_vacuum,can_start_vacuum,can_stop_vacuum,can_discharge_vacuum,can_add_vacuum,can_remove_vacuum");
        this.userRepository.save(admin);

        User createUserOnly = new User();
        createUserOnly.setName("CreateUserOnly");
        createUserOnly.setSurname("CreateUserOnly");
        createUserOnly.setEmail("createUserOnly@gmail.com");
        createUserOnly.setPassword(this.passwordEncoder.encode("createUserOnly"));
        createUserOnly.setPermissions("can_create_users");
        this.userRepository.save(createUserOnly);

        User readUserOnly = new User();
        readUserOnly.setName("ReadUserOnly");
        readUserOnly.setSurname("ReadUserOnly");
        readUserOnly.setEmail("readUserOnly@gmail.com");
        readUserOnly.setPassword(this.passwordEncoder.encode("readUserOnly"));
        readUserOnly.setPermissions("can_read_users");
        this.userRepository.save(readUserOnly);

        User updateUserOnly = new User();
        updateUserOnly.setName("UpdateUserOnly");
        updateUserOnly.setSurname("UpdateUserOnly");
        updateUserOnly.setEmail("updateUserOnly@gmail.com");
        updateUserOnly.setPassword(this.passwordEncoder.encode("updateUserOnly"));
        updateUserOnly.setPermissions("can_update_users");
        this.userRepository.save(updateUserOnly);

        User deleteUserOnly = new User();
        deleteUserOnly.setName("DeleteUserOnly");
        deleteUserOnly.setSurname("DeleteUserOnly");
        deleteUserOnly.setEmail("deleteUserOnly@gmail.com");
        deleteUserOnly.setPassword(this.passwordEncoder.encode("deleteUserOnly"));
        deleteUserOnly.setPermissions("can_delete_users");
        this.userRepository.save(deleteUserOnly);

        User searchVacuumOnly = new User();
        searchVacuumOnly.setName("SearchVacuumOnly");
        searchVacuumOnly.setSurname("SearchVacuumOnly");
        searchVacuumOnly.setEmail("searchVacuumOnly@gmail.com");
        searchVacuumOnly.setPassword(this.passwordEncoder.encode("searchVacuumOnly"));
        searchVacuumOnly.setPermissions("can_search_vacuum");
        this.userRepository.save(searchVacuumOnly);

        User startVacuumOnly = new User();
        startVacuumOnly.setName("StartVacuumOnly");
        startVacuumOnly.setSurname("StartVacuumOnly");
        startVacuumOnly.setEmail("startVacuumOnly@gmail.com");
        startVacuumOnly.setPassword(this.passwordEncoder.encode("startVacuumOnly"));
        startVacuumOnly.setPermissions("can_start_vacuum");
        this.userRepository.save(startVacuumOnly);

        User stopVacuumOnly = new User();
        stopVacuumOnly.setName("StopVacuumOnly");
        stopVacuumOnly.setSurname("StopVacuumOnly");
        stopVacuumOnly.setEmail("stopVacuumOnly@gmail.com");
        stopVacuumOnly.setPassword(this.passwordEncoder.encode("stopVacuumOnly"));
        stopVacuumOnly.setPermissions("can_stop_vacuum");
        this.userRepository.save(stopVacuumOnly);

// VACUUMS:

        Vacuum vacuumAdmin = new Vacuum();
        vacuumAdmin.setName("VacuumAdmin");
        vacuumAdmin.setStatus(Status.STOPPED);
        vacuumAdmin.setActive(true);
        vacuumAdmin.setCreationTime(java.time.LocalDate.now());
        vacuumAdmin.setUser(admin);
        this.vacuumRepository.save(vacuumAdmin);

        Vacuum vacuumCreateUserOnly = new Vacuum();
        vacuumCreateUserOnly.setName("VacuumCreateUserOnly");
        vacuumCreateUserOnly.setStatus(Status.STOPPED);
        vacuumCreateUserOnly.setActive(true);
        vacuumCreateUserOnly.setCreationTime(java.time.LocalDate.now());
        vacuumCreateUserOnly.setUser(createUserOnly);
        this.vacuumRepository.save(vacuumCreateUserOnly);

        Vacuum vacuumReadUserOnly = new Vacuum();
        vacuumReadUserOnly.setName("VacuumReadUserOnly");
        vacuumReadUserOnly.setStatus(Status.STOPPED);
        vacuumReadUserOnly.setActive(true);
        vacuumReadUserOnly.setCreationTime(java.time.LocalDate.now());
        vacuumReadUserOnly.setUser(readUserOnly);
        this.vacuumRepository.save(vacuumReadUserOnly);

        Vacuum vacuumUpdateUserOnly = new Vacuum();
        vacuumUpdateUserOnly.setName("VacuumUpdateUserOnly");
        vacuumUpdateUserOnly.setStatus(Status.STOPPED);
        vacuumUpdateUserOnly.setActive(true);
        vacuumUpdateUserOnly.setCreationTime(java.time.LocalDate.now());
        vacuumUpdateUserOnly.setUser(updateUserOnly);
        this.vacuumRepository.save(vacuumUpdateUserOnly);

        Vacuum vacuumDeleteUserOnly = new Vacuum();
        vacuumDeleteUserOnly.setName("VacuumDeleteUserOnly");
        vacuumDeleteUserOnly.setStatus(Status.STOPPED);
        vacuumDeleteUserOnly.setActive(true);
        vacuumDeleteUserOnly.setCreationTime(java.time.LocalDate.now());
        vacuumDeleteUserOnly.setUser(deleteUserOnly);
        this.vacuumRepository.save(vacuumDeleteUserOnly);

        Vacuum vacuumSearchVacuumOnly = new Vacuum();
        vacuumSearchVacuumOnly.setName("VacuumSearchVacuumOnly");
        vacuumSearchVacuumOnly.setStatus(Status.STOPPED);
        vacuumSearchVacuumOnly.setActive(true);
        vacuumSearchVacuumOnly.setCreationTime(java.time.LocalDate.now());
        vacuumSearchVacuumOnly.setUser(searchVacuumOnly);
        this.vacuumRepository.save(vacuumSearchVacuumOnly);

        Vacuum vacuumStartVacuumOnly = new Vacuum();
        vacuumStartVacuumOnly.setName("VacuumStartVacuumOnly");
        vacuumStartVacuumOnly.setStatus(Status.STOPPED);
        vacuumStartVacuumOnly.setActive(true);
        vacuumStartVacuumOnly.setCreationTime(java.time.LocalDate.now());
        vacuumStartVacuumOnly.setUser(startVacuumOnly);
        this.vacuumRepository.save(vacuumStartVacuumOnly);

        Vacuum vacuumStopVacuumOnly = new Vacuum();
        vacuumStopVacuumOnly.setName("VacuumStopVacuumOnly");
        vacuumStopVacuumOnly.setStatus(Status.STOPPED);
        vacuumStopVacuumOnly.setActive(true);
        vacuumStopVacuumOnly.setCreationTime(java.time.LocalDate.now());
        vacuumStopVacuumOnly.setUser(stopVacuumOnly);
        this.vacuumRepository.save(vacuumStopVacuumOnly);

        // Additional Vacuums for Admin:

        Vacuum vacuumAdmin2 = new Vacuum();
        vacuumAdmin2.setName("VacuumAdmin2");
        vacuumAdmin2.setStatus(Status.RUNNING);
        vacuumAdmin2.setActive(true);
        vacuumAdmin2.setCreationTime(java.time.LocalDate.now().minusDays(1));
        vacuumAdmin2.setUser(admin);
        this.vacuumRepository.save(vacuumAdmin2);

        Vacuum vacuumAdmin3 = new Vacuum();
        vacuumAdmin3.setName("VacuumAdmin3");
        vacuumAdmin3.setStatus(Status.DISCHARGING);
        vacuumAdmin3.setActive(true);
        vacuumAdmin3.setCreationTime(java.time.LocalDate.now().minusDays(2));
        vacuumAdmin3.setUser(admin);
        this.vacuumRepository.save(vacuumAdmin3);

        Vacuum vacuumAdmin4 = new Vacuum();
        vacuumAdmin4.setName("VacuumAdmin4");
        vacuumAdmin4.setStatus(Status.STOPPED);
        vacuumAdmin4.setActive(true);
        vacuumAdmin4.setCreationTime(java.time.LocalDate.now().minusDays(3));
        vacuumAdmin4.setUser(admin);
        this.vacuumRepository.save(vacuumAdmin4);

        Vacuum vacuumAdmin5 = new Vacuum();
        vacuumAdmin5.setName("VacuumAdmin5");
        vacuumAdmin5.setStatus(Status.RUNNING);
        vacuumAdmin5.setActive(true);
        vacuumAdmin5.setCreationTime(java.time.LocalDate.now().minusDays(4));
        vacuumAdmin5.setUser(admin);
        this.vacuumRepository.save(vacuumAdmin5);


        System.out.println("Data loaded!");
    }
}
