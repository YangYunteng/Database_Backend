package fudan.database.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "patient")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int bedId;

    private String name;

    private int grade; //病情评级 1 轻度 2 中度 3 重度

    private int status; //生命体征 1 出院 2 治疗中 3 死亡

    private int jobNumber;

    public Patient() {
    }

    public Patient(int bedId, String name, int grade, int status, int jobNumber) {
        this.bedId = bedId;
        this.name = name;
        this.grade = grade;
        this.status = status;
        this.jobNumber = jobNumber;
    }
}
