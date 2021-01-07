package fudan.database.project.controller.request;

import lombok.Data;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Data
@Controller
public class RecordRequest {
    private int patientID;

    private float temperature;

    private String symptoms;

    private int status;

    private int checkResult;

    private Date date;
}
