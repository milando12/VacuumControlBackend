package rs.raf.demo.dto;

import rs.raf.demo.model.Vacuum;
import rs.raf.demo.responses.VacuumResponse;

public class Mapper {
    public static VacuumResponse vacuumToVacuumResponse(Vacuum vacuum) {
        return new VacuumResponse(
                vacuum.getId(),
                vacuum.getName(),
                vacuum.getStatus().toString(),
                vacuum.getCreationTime().toString()
        );
    }
}
