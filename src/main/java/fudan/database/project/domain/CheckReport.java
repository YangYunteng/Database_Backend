package fudan.database.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "check_report")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class CheckReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int patientId;

    private int checkResult;

    private Date date;

    public CheckReport() {
    }

    public CheckReport(int patientId, int checkResult, Date date) {
        this.patientId = patientId;
        this.checkResult = checkResult;
        this.date = date;
    }
}
