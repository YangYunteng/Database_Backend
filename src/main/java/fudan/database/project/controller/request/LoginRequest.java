package fudan.database.project.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginRequest {
    private int job_number;

    private String password;

    @Autowired

    public int getJob_number() {
        return this.job_number;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJob_number(int job_number) {
        this.job_number = job_number;
    }
}
