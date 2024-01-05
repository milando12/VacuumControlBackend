package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.requests.UserUpdateRequest;
import rs.raf.demo.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


//  Methods:

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_create_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.userService.create(user));
    }

    @GetMapping(value = "/read/all/paginated")
    public ResponseEntity<Page<User>> all(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_read_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.userService.paginate(page, size));
    }

    @GetMapping(value = "/read/all")
    public ResponseEntity<List<User>> all() {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_read_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.userService.findAll());
    }

    @GetMapping(value = "/read/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_update_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.userService.getByEmail(email));
    }


    @DeleteMapping(value = "/delete/{email}")
    public ResponseEntity<?> delete(@PathVariable String email) {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_delete_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        this.userService.delete(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> update(@RequestBody UserUpdateRequest user) {
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new Permission("can_update_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        this.userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
