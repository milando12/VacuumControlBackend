package rs.raf.demo.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.raf.demo.model.enums.Status;

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
