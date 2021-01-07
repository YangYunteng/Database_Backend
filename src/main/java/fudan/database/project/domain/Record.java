package fudan.database.project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "record")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int patientId;

    private float temperature;

    private String symptoms;

    private int status;

    private int checkResult;

    private Date date;

    public Record() {
    }

    public Record(int patientId, float temperature, String symptoms, int status, int checkResult, Date date) {
        this.patientId = patientId;
        this.temperature = temperature;
        this.symptoms = symptoms;
        this.status = status;
        this.checkResult = checkResult;
        this.date = date;
    }
}
