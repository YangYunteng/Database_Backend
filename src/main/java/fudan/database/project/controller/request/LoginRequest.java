package fudan.database.project.controller.request;

import lombok.Data;
import org.springframework.stereotype.Controller;

@Data
@Controller
public class LoginRequest {
    private int jobNumber;
    private String password;
}
