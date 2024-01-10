package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dto.requests.ScheduleRequest;
import rs.raf.demo.dto.responses.VacuumErrorResponse;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.dto.requests.FilterRequest;
import rs.raf.demo.dto.requests.VacuumRequest;
import rs.raf.demo.dto.responses.VacuumResponse;
import rs.raf.demo.model.VacuumError;
import rs.raf.demo.model.enums.VacuumOperation;
import rs.raf.demo.services.VacuumService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vac")
@CrossOrigin
public class VacuumController {
    private final VacuumService vacuumService;

    @Autowired
    public VacuumController(VacuumService vacuumService) {
        this.vacuumService = vacuumService;
    }

//    Methods:

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vacuum> add(@Valid @RequestBody VacuumRequest vacuumRequest){
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_add_vacuum"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.vacuumService.add(vacuumRequest));
    }

    @GetMapping(value = "/remove/{id}")
    public ResponseEntity<Boolean> remove(@PathVariable("id") Long id) {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_remove_vacuum"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.vacuumService.remove(id));
    }

    @PostMapping(value = "/filter")
    public ResponseEntity<List<VacuumResponse>> search(@RequestBody FilterRequest filterRequest) {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_search_vacuum"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.vacuumService.search(filterRequest));
    }

//    Start, Stop and Discharge
    @PostMapping(value = "/start/{id}")
    public ResponseEntity<Void> start(@PathVariable("id") Long id) throws IllegalStateException, InterruptedException{
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_start_vacuum"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        vacuumService.start(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/stop/{id}")
    public ResponseEntity<Void> stop(@PathVariable("id") Long id) throws IllegalStateException{
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_stop_vacuum"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        vacuumService.stop(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/discharge/{id}")
    public ResponseEntity<Void> discharge(@PathVariable("id") Long id) throws IllegalStateException{
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_discharge_vacuum"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        vacuumService.discharge(id);
        return ResponseEntity.ok().build();
    }

//    Scheduler:
    @PostMapping(value = "/schedule/{id}")
    public ResponseEntity<Void> schedule(@PathVariable("id") Long id, @RequestBody ScheduleRequest scheduleRequest) throws IllegalStateException, InterruptedException {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission
                        ("can_"+scheduleRequest.getOperation().toLowerCase()+"_vacuum"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        vacuumService.scheduleOperation(id, VacuumOperation.valueOf(scheduleRequest.getOperation()), scheduleRequest.getScheduledTime());
        return ResponseEntity.ok().build();
    }

//    Errors:
    @GetMapping(value = "/errors")
    public ResponseEntity<List<VacuumErrorResponse>> vacErrors() {
        return ResponseEntity.ok(this.vacuumService.getAllVacuumErrorsForUser());
    }

//    TestMethod:
    @GetMapping(value = "/errors/test")
    public ResponseEntity<List<VacuumError>> getAllVacErrors() {
        return ResponseEntity.ok(this.vacuumService.getAllVacuumErrors());
    }

    @GetMapping(value = "/read/all")
    public ResponseEntity<List<Vacuum>> readAll() {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_search_vacuum"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.vacuumService.findEveryone());
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Vacuum>> all() {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_search_vacuum"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.vacuumService.findAll());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
