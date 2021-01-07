package fudan.database.project.controller;

import fudan.database.project.controller.request.ModifyUserInfoRequest;
import fudan.database.project.domain.User;
import fudan.database.project.repository.UserRepository;
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
public class ModifyUserInfoController {
    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    ModifyUserInfoController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @CrossOrigin
    @PostMapping("/modifyUser")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> modifyUser(@RequestBody ModifyUserInfoRequest modifyUserInfoRequest) {
        int jobNumber = modifyUserInfoRequest.getJobNumber();
        String name = modifyUserInfoRequest.getName();
        String oldPass = modifyUserInfoRequest.getOldPass();
        String newPass = modifyUserInfoRequest.getNewPass();
        String telephone = modifyUserInfoRequest.getTelephone();

        name = HtmlUtils.htmlEscape(name);
        oldPass = HtmlUtils.htmlEscape(oldPass);
        newPass = HtmlUtils.htmlEscape(newPass);
        telephone = HtmlUtils.htmlEscape(telephone);

        User user = userService.findByJobNumber(jobNumber);
        HashMap<String, Object> map = new HashMap<>();
        if (!user.getPassword().equals(oldPass)) {
            map.put("message", "旧密码输入错误");
            map.put("result", 0);
            return ResponseEntity.ok(map);
        } else {
            user.setName(name);
            user.setPassword(newPass);
            user.setTelephone(telephone);
            userRepository.save(user);
            map.put("message", "修改成功");
            map.put("result", 1);
            return ResponseEntity.ok(map);
        }
    }

}
