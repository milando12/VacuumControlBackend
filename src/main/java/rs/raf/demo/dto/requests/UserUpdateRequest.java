package rs.raf.demo.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserUpdateRequest {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String permissions;
}
