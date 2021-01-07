package fudan.database.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "wnurse_patient")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class WNursePatient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int jobNumber;
    private int patientId;

    public WNursePatient() {
    }

    public WNursePatient(int jobNumber, int patientId) {
        this.jobNumber = jobNumber;
        this.patientId = patientId;
    }
}
