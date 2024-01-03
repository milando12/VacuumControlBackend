package rs.raf.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

import java.util.*;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "Shinken";

    private UserRepository userRepository;

    @Autowired
    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();

        claims.put("permissions", getAuthorities(username));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private ArrayList<String> getAuthorities(String email){
        Optional<User> user = Optional.ofNullable(this.userRepository.findByEmail(email));
        ArrayList<String> authorities = new ArrayList<>();
        if(user.isPresent()){
            User u = user.get();
            for (String permission: u.getPermissions().split(",")) {
                authorities.add(permission);;
            }
        }
        return authorities;
    }
}
