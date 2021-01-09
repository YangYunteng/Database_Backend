package fudan.database.project.controller;

import fudan.database.project.controller.request.*;
import fudan.database.project.domain.*;
import fudan.database.project.service.*;
import lombok.Data;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Data
@Controller
public class PatientInfoController {
    private PatientService patientService;
    private UserService userService;
    private BedService bedService;
    private WNursePatientService wNursePatientService;
    private RecordService recordService;
    private CheckReportService checkReportService;
    private MessageService messageService;

    @Autowired
    PatientInfoController(PatientService patientService, UserService userService, BedService bedService, WNursePatientService wNursePatientService, RecordService recordService, CheckReportService checkReportService, MessageService messageService) {
        this.patientService = patientService;
        this.userService = userService;
        this.bedService = bedService;
        this.wNursePatientService = wNursePatientService;
        this.recordService = recordService;
        this.checkReportService = checkReportService;
        this.messageService = messageService;
    }

    @CrossOrigin
    @PostMapping("/patientRegister")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> patientRegister(@RequestBody PatientRegisterRequest patientRegisterRequest) {
        String patientName = patientRegisterRequest.getName();
        int wardNumber = patientRegisterRequest.getGrade();
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
        List<Patient> patients = new ArrayList<>();
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
        List<PatientInfo> patientInfos = new ArrayList<>();
        if (patients != null) {
            for (Patient patient : patients) {
                int id = patient.getId();
                String name = patient.getName();
                int grade = patient.getGrade();
                int status = patient.getStatus();
                String bedInfo = "在隔离区";
                String wNurseName = "尚未安排";
                int temp_jobNumber = -1;
                if (patient.getBedId() != -1) {
                    Bed bed = bedService.findById(patient.getBedId());
                    switch (bed.getWardNumber()) {
                        case 1:
                            bedInfo += "轻度治疗区 ";
                            break;
                        case 2:
                            bedInfo += "中度治疗区 ";
                            break;
                        case 3:
                            bedInfo += "重度治疗区 ";
                            break;
                    }
                    bedInfo += bed.getRoomNumber() + "房" + " " + bed.getBedNumber();
                    wNurseName = userService.findByJobNumber(patient.getJobNumber()).getName();
                    temp_jobNumber = patient.getJobNumber();
                }
                PatientInfo patientInfo = new PatientInfo(id, bedInfo, name, grade, status, wNurseName, temp_jobNumber);
                patientInfos.add(patientInfo);
            }
        }

        map.put("patientData", patientInfos);
        map.put("message", "筛选成功");
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
                List<Bed> tempBeds = bedService.findAllByWardNumberAndStatus(wardNumber, 1);
                for (Bed bed : tempBeds) {
                    WNursePatient wNursePatient = wNursePatientService.findByPatientId(bed.getPatientId());
                    if (wNursePatient.getJobNumber() == jobNumber) {
                        beds.add(bed);
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


    @CrossOrigin
    @PostMapping("/updatePatientInfo")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> updatePatientInfo(@RequestBody UpdatePatientInfoRequest updatePatientInfoRequest) {
        int grade = updatePatientInfoRequest.getGrade();
        int status = updatePatientInfoRequest.getStatus();
        int patientId = updatePatientInfoRequest.getPatientID();
        Patient patient = patientService.findById(patientId);
        patient.setStatus(status);
        patient.setGrade(grade);
        if (status == 3) {
            patientService.patientDead(patient);
        }
        patientService.getPatientRepository().save(patient);
        patientService.AutoReferral();
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "修改成功");
        map.put("result", 1);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin
    @PostMapping("/checkByDoctor")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> checkByDoctor(@RequestBody CheckByDoctorRequest checkByDoctorRequest) {
        int patientID = checkByDoctorRequest.getPatientID();
        int checkResult = checkByDoctorRequest.getCheckResult();
        Date date = checkByDoctorRequest.getDate();
        checkReportService.getCheckReportRepository().save(new CheckReport(patientID, checkResult, date));
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "添加成功");
        map.put("result", 1);
        if (patientService.canLeaveHospital(patientID)) {
            Patient patient = patientService.findById(patientID);
            Bed bed = bedService.findById(patientID);
            int wardNumber = bed.getWardNumber();
            User user = userService.findByWardNumberAndType(wardNumber, 1);
            Message message = new Message(user.getJobNumber(), 1);
            if (wardNumber == 1) {
                message.setMessage("ID: " + patient.getId() + " Name: " + patient.getName() + " 满足出院条件");
            } else {
                message.setMessage("ID: " + patient.getId() + " Name: " + patient.getName() + " 病情好转可以考虑降级");
            }
            messageService.getMessageRepository().save(message);
        }
        return ResponseEntity.ok(map);
    }

    @CrossOrigin
    @PostMapping("/leaveHospital")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> updatePatientInfo(@RequestBody LeaveHospitalRequest leaveHospitalRequest) {
        HashMap<String, Object> map = new HashMap<>();
        if (patientService.canLeaveHospital(leaveHospitalRequest.getPatientID())) {
            Patient patient = patientService.findById(leaveHospitalRequest.getPatientID());
            Bed bed = bedService.findById(patient.getBedId());
            patient.setStatus(1);
            patient.setGrade(0);
            patient.setBedId(-1);
            patient.setJobNumber(-1);
            bed.setPatientId(-1);
            bed.setStatus(0);
            bedService.getBedRepository().save(bed);
            patientService.getPatientRepository().save(patient);
            wNursePatientService.deleteByPatientId(patient.getId());
            map.put("message", "成功出院");
            map.put("result", 1);
        } else {
            map.put("message", "不满足出院条件");
            map.put("result", 0);
        }
        return ResponseEntity.ok(map);
    }


    @Data
    class PatientInfo {
        private int id;
        private String bed;
        private String name;
        private int grade;
        private int status;
        private String wNurseName;
        private int jobNumber;

        PatientInfo() {
        }

        PatientInfo(int id, String bed, String name, int grade, int status, String wNurseName, int jobNumber) {
            this.id = id;
            this.bed = bed;
            this.name = name;
            this.grade = grade;
            this.status = status;
            this.wNurseName = wNurseName;
            this.jobNumber = jobNumber;
        }
    }
}
