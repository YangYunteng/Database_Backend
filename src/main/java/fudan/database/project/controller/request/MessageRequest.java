package fudan.database.project.controller.request;


import lombok.Data;
import org.springframework.stereotype.Controller;

@Controller
@Data
public class MessageRequest {
    private int jobNumber;
}
