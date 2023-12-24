package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private TaskScheduler taskScheduler;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, TaskScheduler taskScheduler) {
        this.passwordEncoder = passwordEncoder;

        this.userRepository = userRepository;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.userRepository.findByEmail(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }
// you can set permissions here
        return new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword()
//                , new ArrayList<>());
                , getAuthorities(myUser.getEmail()));
//        Arrays.asList((GrantedAuthority)() -> "READ", (GrantedAuthority)()->"WRITE")
    }

    private ArrayList<GrantedAuthority> getAuthorities(String email){
        Optional<User> user = Optional.ofNullable(this.userRepository.findByEmail(email));
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        if(user.isPresent()){
            User u = user.get();
            for (String permission: u.getPermissions().split(",")) {
                System.out.println("permission: " + permission);
                authorities.add(new Permission(permission));;
            }
        }
        return authorities;
    }


    public User create(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public Page<User> paginate(Integer page, Integer size) {
        return this.userRepository.findAll(PageRequest.of(page, size));
    }

    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }

    public User update(Long id, User user) {
        User oldUser = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setId(oldUser.getId());
        user.setPassword(oldUser.getPassword());
        return this.userRepository.save(user);
    }
}
