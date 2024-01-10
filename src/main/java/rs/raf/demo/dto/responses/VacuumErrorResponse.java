package rs.raf.demo.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacuumErrorResponse {
    private Long id;
    private String message;
    private String operation;
    private String date;
}