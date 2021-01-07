package fudan.database.project.controller;

import fudan.database.project.controller.request.NurseInfoRequest;
import fudan.database.project.domain.Patient;
import fudan.database.project.domain.User;
import fudan.database.project.domain.WNursePatient;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class NurseInfoController {
    private UserService userService;
    private WNursePatientService wNursePatientService;
    private PatientService patientService;

    @Autowired
    NurseInfoController(UserService userService, WNursePatientService wNursePatientService, PatientService patientService) {
        this.userService = userService;
        this.wNursePatientService = wNursePatientService;
        this.patientService = patientService;
    }

    @CrossOrigin
    @PostMapping("/wnurseInfo")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> wnurseInfo(@RequestBody NurseInfoRequest nurseInfoRequest) {
        int wardNumber = nurseInfoRequest.getWardNumber();
        List<User> users = userService.findAllByWardNumberAndType(wardNumber, 3);
        List<WNurseInfo> wWNurseInfos = new ArrayList<>();
        for (User user : users) {
            int jobNumber = user.getJobNumber();
            String name = user.getName();
            String telephone = user.getTelephone();

            List<WNursePatient> wNursePatients = wNursePatientService.findAllByJobNumber(jobNumber);
            StringBuilder patients = new StringBuilder();

            if (wNursePatients.size() > 0) {
                for (WNursePatient wNursePatient : wNursePatients) {
                    patients.append(patientService.findById(wNursePatient.getPatientId()).getName());
                    patients.append("  ");
                }
            }
//            WNurse wNurse = wNurseService.findByUser(user);
//           List<Patient> patients = wNurse.getPatients();
            WNurseInfo wNurseInfo = new WNurseInfo(jobNumber, name, telephone, patients.toString());
            wWNurseInfos.add(wNurseInfo);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("wnurseInfo", wWNurseInfos);
        map.put("result", 1);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin
    @PostMapping("/hnurseInfo")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> hnurseInfo(@RequestBody NurseInfoRequest nurseInfoRequest) {
        int wardNumber = nurseInfoRequest.getWardNumber();
        User user = userService.findByWardNumberAndType(wardNumber, 2);
        HNurseInfo hNurseInfo = new HNurseInfo(user.getJobNumber(), user.getName(), user.getTelephone());
        List<HNurseInfo> hNurseInfos = new ArrayList<>();
        hNurseInfos.add(hNurseInfo);
        HashMap<String, Object> map = new HashMap<>();
        map.put("hnurseInfo", hNurseInfos);
        map.put("result", 1);
        return ResponseEntity.ok(map);
    }

    @Data
    class WNurseInfo {
        private int jobNumber;
        private String name;
        private String telephone;
        private String patients;

        WNurseInfo(int jobNumber, String name, String telephone, String patients) {
            this.jobNumber = jobNumber;
            this.name = name;
            this.telephone = telephone;
            this.patients = patients;
        }
    }

    @Data
    class HNurseInfo {
        private int jobNumber;
        private String name;
        private String telephone;

        HNurseInfo(int jobNumber, String name, String telephone) {
            this.jobNumber = jobNumber;
            this.name = name;
            this.telephone = telephone;
        }
    }


}



