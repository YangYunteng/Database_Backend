package fudan.database.project.controller.request;

import lombok.Data;
import org.springframework.stereotype.Controller;

@Data
@Controller
public class AddWNurseRequest {
    private int wardNumber;
    private String name;
    private String telephone;
}
