package fudan.database.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jobNumber;

    private String name;

    private String password;

    private int type;// 1 代表医生 2 代表护士长 3 代表病房护士 4代表急诊护士

    private int wardNumber;

    private String telephone;

    public User() {
    }

    public User( String name, String password, int type, int wardNumber, String telephone) {
        this.name = name;
        this.password = password;
        this.type = type;
        this.wardNumber = wardNumber;
        this.telephone = telephone;
    }
}
