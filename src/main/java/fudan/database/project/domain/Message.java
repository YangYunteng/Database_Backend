package fudan.database.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "message")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int jobNumber;

    private String message;

    private int status;// 0 未确认 1 收到

    public Message() {
    }

    public Message(int jobNumber, String message, int status) {
        this.jobNumber = jobNumber;
        this.message = message;
        this.status = status;
    }

    public Message(int jobNumber, int status) {
        this.jobNumber = jobNumber;
        this.status = status;
    }
}
