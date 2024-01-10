package rs.raf.demo.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {
    String scheduledTime; // "yyyy-MM-dd HH:mm:ss"
    String operation;
}
