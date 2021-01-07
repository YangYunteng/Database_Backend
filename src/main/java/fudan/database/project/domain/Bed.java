package fudan.database.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Id;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bed")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int bedNumber;

    private int roomNumber;

    private int wardNumber;

    private int status;//0 空 1 有

    private int patientId;

    public Bed() {
    }

    public Bed(int bedNumber, int roomNumber, int wardNumber, int status, int patientId) {
        this.bedNumber = bedNumber;
        this.roomNumber = roomNumber;
        this.wardNumber = wardNumber;
        this.status = status;
        this.patientId = patientId;
    }

}
