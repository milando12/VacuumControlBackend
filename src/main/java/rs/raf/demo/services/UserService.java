package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.requests.UserUpdateRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.userRepository.findByEmail(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }
// you can set permissions here
        return new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword()
                , getAuthorities(myUser.getEmail()));
    }

    private ArrayList<GrantedAuthority> getAuthorities(String email){
        Optional<User> user = Optional.ofNullable(this.userRepository.findByEmail(email));
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        if(user.isPresent()){
            User u = user.get();
            if(u.getPermissions() == null) {
                return authorities;
            }
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

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public void delete(String email) {
        this.userRepository.deleteByEmail(email);
    }

    public User update(UserUpdateRequest user) {
//          would be more elegant if I made a mapper class
        Optional<User> u = this.userRepository.findById(user.getId());
        if(!u.isPresent()) {
            return null;
        }else {
            u.get().setName(user.getName());
            u.get().setSurname(user.getSurname());
            u.get().setEmail(user.getEmail());
            u.get().setPermissions(user.getPermissions());
            return this.userRepository.save(u.get());
        }
    }

    public User getByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
