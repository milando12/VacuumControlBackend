package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import rs.raf.demo.model.enums.VacuumOperation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VacuumError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Message is mandatory")
    private String message;

    @Column
    @NotBlank(message = "Operation is mandatory")
    private VacuumOperation operation;

    @Column
    @NotBlank(message = "Date is mandatory")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "vacuum_id", referencedColumnName = "id")
    @JsonBackReference
    private Vacuum vacuum;
}