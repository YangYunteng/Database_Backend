package fudan.database.project.controller;

import fudan.database.project.controller.request.PatientQueryRequest;
import fudan.database.project.controller.request.PatientRegisterRequest;
import fudan.database.project.domain.*;
import fudan.database.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class PatientRegisterController {
    private PatientService patientService;
    private UserService userService;
    private BedService bedService;
    private WNursePatientService wNursePatientService;

    @Autowired
    PatientRegisterController(PatientService patientService, UserService userService, BedService bedService, WNursePatientService wNursePatientService) {
        this.patientService = patientService;
        this.userService = userService;
        this.bedService = bedService;
        this.wNursePatientService = wNursePatientService;
    }

    @CrossOrigin
    @PostMapping("/patientRegister")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> patientRegister(@RequestBody PatientRegisterRequest patientRegisterRequest) {
        String patientName = patientRegisterRequest.getName();
        int wardNumber = patientRegisterRequest.getGrade();
        patientName = HtmlUtils.htmlEscape(patientName);
        HashMap<String, Object> map = new HashMap<>();
        List<User> wnurses = userService.findAllByWardNumberAndType(wardNumber, 3);
        User wNurse = null;
        for (User tempWNurse : wnurses) {
            if (wNursePatientService.findAllByJobNumber(tempWNurse.getJobNumber()).size() < 3) {
                wNurse = tempWNurse;
                break;
            }
        }
        List<Bed> beds = bedService.findAllByWardNumber(wardNumber);
        Bed bed = null;
        for (Bed temp : beds) {
            if (temp.getStatus() == 0) {
                bed = temp;
                break;
            }
        }
        if (bed == null) {
            //在隔离区 status=4
            Patient patient = new Patient(-1, patientName, wardNumber, 4, -1);
            patientService.getPatientRepository().save(patient);
            map.put("message", "无空余床位，转入隔离区");
            map.put("result", 0);
        } else if (wNurse == null) {
            Patient patient = new Patient(-1, patientName, wardNumber, 4, -1);
            patientService.getPatientRepository().save(patient);
            map.put("message", "没有护士，转入隔离区");
            map.put("result", 0);
        } else {
            Patient patient = new Patient(bed.getId(), patientName, wardNumber, 2, wNurse.getJobNumber());
            patientService.getPatientRepository().save(patient);
            bed.setStatus(1);
            bed.setPatientId(patient.getId());
            bedService.getBedRepository().save(bed);
            WNursePatient wNursePatient = new WNursePatient(wNurse.getJobNumber(), patient.getId());
            wNursePatientService.getWNursePatientRepository().save(wNursePatient);
            map.put("message", "办理住院");
            map.put("result", 1);
        }
        return ResponseEntity.ok(map);
    }

    @CrossOrigin
    @PostMapping("/patientInfo")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> queryPatientInfo(@RequestBody PatientQueryRequest patientQueryRequest) {
        int jobNumber = patientQueryRequest.getJobNumber();
        int queryCondition = patientQueryRequest.getQueryCondition();
        User user = userService.findByJobNumber(jobNumber);
        int wardNumber = user.getWardNumber();
        int type = user.getType();
        HashMap<String, Object> map = new HashMap<>();
        List<Patient> patients;
        switch (queryCondition) {
            case 0:
                patients = queryPatientWithNoCondition(wardNumber, type, jobNumber);
                break;
            case 1:
                patients = queryPatientCanLeave(wardNumber, type);
                break;
            case 2:
                patients = queryPatientCanRefer(wardNumber, type, jobNumber);
                break;
            case 3:
                patients = queryPatientByWard(1);
                break;
            case 4:
                patients = queryPatientByWard(2);
                break;
            case 5:
                patients = queryPatientByWard(3);
                break;
            case 6:
                patients = queryPatientInIsolation();
                break;
            case 7:
                patients = queryPatientByGrade(1);
                break;
            case 8:
                patients = queryPatientByGrade(2);
                break;
            case 9:
                patients = queryPatientByGrade(3);
                break;
            default:
                patients = queryPatientWithNoCondition(wardNumber, type, jobNumber);
                break;
        }
        map.put("patientData", patients);
        map.put("result", 1);
        return ResponseEntity.ok(map);
    }

    //无筛选查询 0
    private List<Patient> queryPatientWithNoCondition(int wardNumber, int type, int jobNumber) {
        List<Bed> beds = new ArrayList<>();
        List<Patient> patients = new ArrayList<>();
        switch (type) {
            case 1:
                beds = bedService.findAllByWardNumberAndStatus(wardNumber, 1);
                break;
            case 2:
                beds = bedService.findAllByWardNumberAndStatus(wardNumber, 1);
                break;
            case 3:
                beds = bedService.findAllByWardNumberAndStatus(wardNumber, 1);
                for (Bed bed : beds) {
                    WNursePatient wNursePatient = wNursePatientService.findByPatientId(bed.getPatientId());
                    if (wNursePatient.getJobNumber() != jobNumber) {
                        beds.remove(bed);
                    }
                }
                break;
            case 4:
                beds = bedService.findAllByStatus(1);
                break;
            default:
                beds = bedService.findAllByStatus(1);
                break;
        }
        for (Bed bed : beds) {
            int patientId = bed.getPatientId();
            patients.add(patientService.findById(patientId));
        }
        return patients;
    }

    //满足出院条件但没有出院（还没有每日记录登记） 1
    private List<Patient> queryPatientCanLeave(int wardNumber, int type) {
        return null;
    }

    //待转入其他病区  2
    private List<Patient> queryPatientCanRefer(int wardNumber, int type, int jobNumber) {
        List<Bed> beds = bedService.findAllByWardNumber(wardNumber);
        List<Patient> patients = new ArrayList<>();
        for (Bed bed : beds) {
            int patientId = bed.getPatientId();
            Patient patient = patientService.findById(patientId);
            if (patient.getGrade() != wardNumber) {
                patients.add(patient);
            }
        }
        if (type == 3) {
            for (Patient patient : patients) {
                if (patient.getJobNumber() != jobNumber) {
                    patients.remove(patient);
                }
            }
        }
        return patients;
    }

    //治疗区筛选
    private List<Patient> queryPatientByWard(int wardNumber) {
        List<Bed> beds = bedService.findAllByWardNumber(wardNumber);
        List<Patient> patients = new ArrayList<>();
        for (Bed bed : beds) {
            if (bed.getStatus() == 1) {
                Patient patient = patientService.findById(bed.getPatientId());
                patients.add(patient);
            }
        }
        return patients;
    }

    //在隔离区
    private List<Patient> queryPatientInIsolation() {
        return patientService.findAllByStatus(4);
    }

    //根据病情
    private List<Patient> queryPatientByGrade(int grade) {
        return patientService.findAllByGrade(grade);
    }
}
