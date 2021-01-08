package fudan.database.project.controller.request;

import lombok.Data;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Data
@Controller
public class CheckByDoctorRequest {
    private int patientID;
    private int checkResult;
    private Date date;
}
