package rs.raf.demo.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    private String name;
    private List<String> statuses;
    private String dateFrom;    // yyyy-MM-dd
    private String dateTo;
}
