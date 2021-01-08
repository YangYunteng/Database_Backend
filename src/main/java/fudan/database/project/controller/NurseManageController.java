package fudan.database.project.controller;

import com.sun.org.apache.bcel.internal.generic.NEW;
import fudan.database.project.controller.request.AddWNurseRequest;
import fudan.database.project.controller.request.DeleteNurseRequest;
import fudan.database.project.domain.Bed;
import fudan.database.project.domain.Patient;
import fudan.database.project.domain.User;
import fudan.database.project.domain.WNursePatient;
import fudan.database.project.service.BedService;
import fudan.database.project.service.PatientService;
import fudan.database.project.service.UserService;
import fudan.database.project.service.WNursePatientService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Data
@Controller
public class NurseManageController {
    private UserService userService;
    private BedService bedService;
    private PatientService patientService;
    private WNursePatientService wNursePatientService;

    @Autowired
    NurseManageController(UserService userService, BedService bedService, PatientService patientService, WNursePatientService wNursePatientService) {
        this.userService = userService;
        this.bedService = bedService;
        this.patientService = patientService;
        this.wNursePatientService = wNursePatientService;
    }

    @CrossOrigin
    @PostMapping("/addNurse")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> addWNurse(@RequestBody AddWNurseRequest addWNurseRequest) {
        int wardNumber = addWNurseRequest.getWardNumber();
        String name = addWNurseRequest.getName();
        String telephone = addWNurseRequest.getTelephone();
        String password = "12345";
        int type = 3;
        User user = new User(name, password, type, wardNumber, telephone);
        userService.getUserRepository().save(user);
        patientService.AutoReferral();
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "添加成功");
        map.put("result", 1);
        patientService.AutoReferral();
        return ResponseEntity.ok(map);
    }

    @CrossOrigin
    @PostMapping("/deleteNurse")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> deleteWNurse(@RequestBody DeleteNurseRequest deleteNurseRequest) {
        int jobNumber = deleteNurseRequest.getJobNumber();
        List<WNursePatient> wNursePatients = wNursePatientService.findAllByJobNumber(jobNumber);
        HashMap<String, Object> map = new HashMap<>();
        if (wNursePatients.size() > 0) {
            map.put("message", "护士有病人，无法删除");
            map.put("result", 0);
        } else {
            userService.deleteByJobNumber(jobNumber);
            map.put("message", "删除成功");
            map.put("result", 1);
        }
        return ResponseEntity.ok(map);
    }

}
