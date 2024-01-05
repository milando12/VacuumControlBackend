package rs.raf.demo.model;

import lombok.*;
import rs.raf.demo.model.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vacuum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column
    private Boolean active;

    //    status Enum(RUNNING, STOPPED, DISCHARGING)
    @Column()
    private Status status;

    @Column
    private LocalDate creationTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "vacuum", cascade = CascadeType.ALL)
    private List<VacuumError> vacuumErrors;
}
