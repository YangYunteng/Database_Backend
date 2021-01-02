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

    private int bed_number;

    private int room_number;

    private int ward_number;

    private int status;//0 空 1 有

    @OneToOne
    private Patient patient;

}
