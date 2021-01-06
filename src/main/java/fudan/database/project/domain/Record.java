package fudan.database.project.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "record")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Patient patient;

    private float temperature;

    private int status;

    private int checkResult;
}
