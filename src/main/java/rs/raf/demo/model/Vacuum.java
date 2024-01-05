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
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "vacuum", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<VacuumError> vacuumErrors;
}
