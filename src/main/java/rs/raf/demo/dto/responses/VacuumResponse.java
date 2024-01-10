package rs.raf.demo.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VacuumResponse {
    private Long id;
    private String name;
    private String status;
    private String creationTime;
}
