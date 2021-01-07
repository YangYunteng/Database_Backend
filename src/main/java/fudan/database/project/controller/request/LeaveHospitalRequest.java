package fudan.database.project.controller.request;

import lombok.Data;
import org.springframework.stereotype.Controller;

@Data
@Controller
public class LeaveHospitalRequest {
    private int patientID;
}
