package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.requests.VacuumRequest;
import rs.raf.demo.services.UserService;
import rs.raf.demo.services.VacuumService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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

    @GetMapping(value = "/read/all")
    public ResponseEntity<List<Vacuum>> all() {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_search_vacuum"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.vacuumService.findAll());
    }

}
