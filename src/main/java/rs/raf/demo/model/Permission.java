package rs.raf.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Permission implements GrantedAuthority {
    private String permisson;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission perm = (Permission) o;
        return Objects.equals(permisson, perm.permisson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permisson);
    }

    @Override
    public String getAuthority() {
        return permisson;
    }
}
