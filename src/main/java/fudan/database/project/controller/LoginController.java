package fudan.database.project.controller;

import fudan.database.project.controller.request.LoginRequest;
import fudan.database.project.domain.User;
import fudan.database.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;

@Controller
public class LoginController {

    private UserService userService;

    @Autowired
    LoginController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        int job_number = loginRequest.getJobNumber();
        User user = userService.getByJob_Number(job_number);
        System.out.println(loginRequest.getJobNumber()+"  "+ HtmlUtils.htmlEscape(loginRequest.getPassword()));
        HashMap<String, Object> map = new HashMap<>();

        if (!userService.isExist(job_number)) {
            map.put("message", "用户不存在");
            map.put("result", 0);
            return ResponseEntity.ok(map);
        } else if (!user.getPassword().equals(loginRequest.getPassword())) {
            map.put("message", "用户名或密码错误");
            map.put("result", 0);
            return ResponseEntity.ok(map);
        }

        map.put("message", "登录成功");
        map.put("result", 1);
        map.put("jobNumber", user.getJobNumber());
        map.put("name", user.getName());
        map.put("type", user.getType());
        map.put("wardNumber", user.getWardNumber());
        map.put("telephone", user.getTelephone());
        return ResponseEntity.ok(map);
    }

}
