package fudan.database.project.controller.request;

import lombok.Data;
import org.springframework.stereotype.Controller;

@Data
@Controller
public class UpdatePatientInfoRequest {
    private int patientID;
    private int grade;
    private int status;
}
