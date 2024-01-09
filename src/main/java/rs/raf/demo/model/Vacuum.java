package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import rs.raf.demo.model.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "vacuums")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vacuum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

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

    // number of times vacuum has been used since last discharge
    @Column
    private Integer usageCount;

    // boolean carrying information whether some operation is in progress
    // it should not be stored in the database and is false by default, if it is true, than the vacuum is busy
    @Column(name = "busyVac")
    private Boolean busy;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "vacuum", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<VacuumError> vacuumErrors;
}
