package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.dto.Mapper;
import rs.raf.demo.dto.responses.VacuumErrorResponse;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumError;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.model.enums.VacuumOperation;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumErrorRepository;
import rs.raf.demo.repositories.VacuumRepository;
import rs.raf.demo.dto.requests.FilterRequest;
import rs.raf.demo.dto.requests.VacuumRequest;
import rs.raf.demo.dto.responses.VacuumResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class VacuumService {
    private VacuumRepository vacuumRepository;
    private UserRepository userRepository;
    private TaskScheduler taskScheduler;
    private VacuumErrorRepository vacuumErrorRepository;

    @Autowired
    public VacuumService(UserRepository userRepository, VacuumRepository vacuumRepository, TaskScheduler taskScheduler, VacuumErrorRepository vacuumErrorRepository) {
        this.vacuumRepository = vacuumRepository;
        this.userRepository = userRepository;
        this.taskScheduler = taskScheduler;
        this.vacuumErrorRepository = vacuumErrorRepository;
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
    public List<VacuumResponse> search(FilterRequest filterRequest) {
    String name = filterRequest.getName();
    List<Status> statuses = getStatuses(filterRequest.getStatuses());
    String dateFrom = filterRequest.getDateFrom();  // yyyy-MM-dd
    String dateTo = filterRequest.getDateTo();
    Long userId = getUser().getId();

    return this.vacuumRepository.findAllByActiveIsTrueAndUser_Id(userId).stream()
            .filter(vacuum -> name == null || vacuum.getName().toLowerCase().contains(name.toLowerCase()))
            .filter(vacuum -> statuses == null || statuses.isEmpty() || statuses.contains(vacuum.getStatus()))
            .filter(vacuum -> dateFrom == null || isValidDate(dateFrom) || vacuum.getCreationTime().isAfter(LocalDate.parse(dateFrom)))
            .filter(vacuum -> dateTo == null || isValidDate(dateTo) || vacuum.getCreationTime().isBefore(LocalDate.parse(dateTo)))
            .map(Mapper::vacuumToVacuumResponse)
            .collect(Collectors.toList());
}

// Start, Stop, Discharge
    //TODO: Add time standard deviation
    //TODO: catch ObjectOptimisticLockingFailureException and logg it, just a try-catching block around the save method
    @Async
    public CompletableFuture<Void> start(Long id) throws IllegalStateException{
        System.out.println("Starting vacuum with id: " + id);
        Vacuum vacuum = vacuumRepository.findById(id).orElse(null);

        if (vacuum == null || !vacuum.getStatus().equals(Status.STOPPED) || vacuum.getBusy()) {
            logError(vacuum, VacuumOperation.START, (vacuum.getBusy())? "Vacuum was busy." : "Vacuum was not stopped.");
            throw new IllegalStateException("Invalid operation on the vacuum.");
        }

        vacuum.setBusy(true);
        vacuumRepository.save(vacuum);
//        System.out.println("Busy set to true "+ vacuumRepository.findById(id));

        simulateDelay(15);

        vacuum= vacuumRepository.findById(id).orElse(null);
        vacuum.setStatus(Status.RUNNING);
        vacuum.setBusy(false);
        vacuumRepository.save(vacuum);
//        System.out.println("Busy set to false "+ vacuumRepository.findById(id));

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> stop(Long id) throws IllegalStateException{
        System.out.println("Stopping vacuum with id: " + id);
        Vacuum vacuum = vacuumRepository.findById(id).orElse(null);

        if (vacuum == null || !vacuum.getStatus().equals(Status.RUNNING) || vacuum.getBusy()) {
            logError(vacuum, VacuumOperation.STOP, (vacuum.getBusy())? "Vacuum was busy." : "Vacuum was not running.");
            throw new IllegalStateException("Invalid operation on the vacuum.");
        }

        vacuum.setBusy(true);
        vacuumRepository.save(vacuum);

        simulateDelay(15);

        vacuum= vacuumRepository.findById(id).orElse(null);
        vacuum.setStatus(Status.STOPPED);
        vacuum.setBusy(false);
        vacuumRepository.save(vacuum);

        if(vacuum.getUsageCount()== 2){
            discharge(id);
        }
        else{
            vacuum= vacuumRepository.findById(id).orElse(null);
            vacuum.setUsageCount(vacuum.getUsageCount()+1);
            vacuumRepository.save(vacuum);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> discharge(Long id) throws IllegalStateException{
        System.out.println("Discharging vacuum with id: " + id);
        Vacuum vacuum = vacuumRepository.findById(id).orElse(null);

        if (vacuum == null || !vacuum.getStatus().equals(Status.STOPPED) || vacuum.getBusy()) {
            logError(vacuum, VacuumOperation.DISCHARGE, (vacuum.getBusy())? "Vacuum was busy." : "Vacuum was not stopped.");
            throw new IllegalStateException("Invalid operation on the vacuum.");
        }

        vacuum.setBusy(true);
        vacuumRepository.save(vacuum);

        simulateDelay(15);

        vacuum= vacuumRepository.findById(id).orElse(null);
        vacuum.setStatus(Status.DISCHARGING);
        vacuumRepository.save(vacuum);

        simulateDelay(15);

        vacuum= vacuumRepository.findById(id).orElse(null);
        vacuum.setStatus(Status.STOPPED);
        vacuum.setUsageCount(0);
        vacuum.setBusy(false);
        vacuumRepository.save(vacuum);

        return CompletableFuture.completedFuture(null);
    }

    private void simulateDelay(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

//    Scheduler
    public CompletableFuture<Void> scheduleOperation(Long id, VacuumOperation operation, String scheduledTimeString) {
        Vacuum vacuum = vacuumRepository.findById(id).orElse(null);

        LocalDateTime scheduledTime = LocalDateTime.parse(scheduledTimeString,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Schedule the operation
        taskScheduler.schedule(
                () -> executeScheduledOperation(vacuum, operation),
                new CronTrigger(getCronExpression(scheduledTime)));
        System.out.println("Scheduled operation: " + operation + " on vacuum with id: " + id + " at: " + scheduledTime + " with cron: " + getCronExpression(scheduledTime));

        return CompletableFuture.completedFuture(null);
    }

    private String getCronExpression(LocalDateTime localDateTime) {
        return String.format("%d %d %d %d %d ?",
                localDateTime.getSecond(),
                localDateTime.getMinute(),
                localDateTime.getHour(),
                localDateTime.getDayOfMonth(),
                localDateTime.getMonthValue());
    }

    private void executeScheduledOperation(Vacuum vacuum, VacuumOperation operation) {
        switch (operation) {
            case START:
                start(vacuum.getId());
                break;
            case STOP:
                stop(vacuum.getId());
                break;
            case DISCHARGE:
                discharge(vacuum.getId());
                break;
            default:
                System.out.println("Unsupported operation. Switch");
//                logError(vacuum, operation, "Unsupported operation.");
        }
    }

    private void logError(Vacuum vacuum, VacuumOperation operation, String errorMessage) {
        System.out.println("Logging error for vacuum: " + vacuum.getId());
        VacuumError vacuumError = new VacuumError();
        vacuumError.setVacuum(vacuum);
        vacuumError.setOperation(operation);
        vacuumError.setDate(LocalDate.now());
        vacuumError.setMessage(errorMessage);
        vacuumErrorRepository.save(vacuumError);
    }

//    Vacuum Errors
    public List<VacuumErrorResponse> getAllVacuumErrorsForUser(){
        User user = getUser();
        List<Vacuum> allVacuums = vacuumRepository.findAll();

        List<Vacuum> userVacuums = new ArrayList<>();
        for (Vacuum vacuum:allVacuums) {
            if (vacuum.getUser().getId().equals(user.getId())){
                userVacuums.add(vacuum);
            }
        }

        List<VacuumError> userVacuumErrors = new ArrayList<>();

        for (Vacuum vacuum: userVacuums) {
            for (VacuumError vacuumError: vacuum.getVacuumErrors()) {
                userVacuumErrors.add(vacuumError);
            }
        }

//        System.out.println("USER VACUUM ERRORS:\t"+userVacuumErrors);
        return userVacuumErrors.stream().map(Mapper::vacuumErrorToVacuumErrorResponse).collect(Collectors.toList());
    }

    // Test
    public List<Vacuum> findEveryone() {
        return this.vacuumRepository.findAll();
    }

    public List<VacuumError> getAllVacuumErrors(){
        return this.vacuumErrorRepository.findAll();
    }


//  Complementary methods:

    private User getUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("EMAIL:\t"+email);
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
