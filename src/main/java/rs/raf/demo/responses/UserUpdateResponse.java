package rs.raf.demo.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserUpdateResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String permissions;
}
