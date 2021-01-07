package fudan.database.project.controller.request;

import lombok.Data;
import org.springframework.stereotype.Controller;

@Data
@Controller
public class ModifyUserInfoRequest {
    private int jobNumber;
    private String name;
    private String oldPass;
    private String newPass;
    private String telephone;

}
