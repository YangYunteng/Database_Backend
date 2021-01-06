package fudan.database.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "check_report")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class CheckReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Patient patient;

    private int checkResult;

    private LocalDate date;

    private int grade;
}
