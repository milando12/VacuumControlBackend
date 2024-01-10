package rs.raf.demo.dto;

import rs.raf.demo.dto.responses.VacuumErrorResponse;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.dto.responses.VacuumResponse;
import rs.raf.demo.model.VacuumError;

public class Mapper {
    public static VacuumResponse vacuumToVacuumResponse(Vacuum vacuum) {
        return new VacuumResponse(
                vacuum.getId(),
                vacuum.getName(),
                vacuum.getStatus().toString(),
                vacuum.getCreationTime().toString()
        );
    }

    public static VacuumErrorResponse vacuumErrorToVacuumErrorResponse(VacuumError vacuumError) {
        return new VacuumErrorResponse(
                vacuumError.getId(),
                vacuumError.getMessage(),
                vacuumError.getOperation().toString(),
                vacuumError.getDate().toString()
        );
    }
}
