package fudan.database.project.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginRequest {
    private int jobNumber;

    private String password;

    @Autowired

    public int getJobNumber() {
        return this.jobNumber;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }
}
