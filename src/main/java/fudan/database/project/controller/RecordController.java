package fudan.database.project.controller;

import fudan.database.project.controller.request.RecordRequest;
import fudan.database.project.domain.*;
import fudan.database.project.service.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;

@Data
@Controller
public class RecordController {
    private RecordService recordService;
    private CheckReportService checkReportService;
    private UserService userService;
    private PatientService patientService;
    private BedService bedService;
    private MessageService messageService;

    @Autowired
    RecordController(RecordService recordService, CheckReportService checkReportService, UserService userService, PatientService patientService, BedService bedService, MessageService messageService) {
        this.recordService = recordService;
        this.checkReportService = checkReportService;
        this.userService = userService;
        this.patientService = patientService;
        this.messageService = messageService;
        this.bedService = bedService;
    }

    @CrossOrigin
    @PostMapping("/dailyRegister")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> dailyRegister(@RequestBody RecordRequest recordRequest) {
        int patientId = recordRequest.getPatientID();
        float temperature = recordRequest.getTemperature();
        String symptoms = recordRequest.getSymptoms();
        int status = recordRequest.getStatus();
        int checkResult = recordRequest.getCheckResult();
        Date date = recordRequest.getDate();
        if (status == 3) {
            patientService.patientDead(patientService.findById(patientId));
            patientService.AutoReferral();
        } else {
            Record record = new Record(patientId, temperature, symptoms, status, date);
            recordService.getRecordRepository().save(record);
            checkReportService.getCheckReportRepository().save(new CheckReport(patientId, checkResult, date));
        }

        if (patientService.canLeaveHospital(patientId)) {
            Patient patient = patientService.findById(patientId);
            User user = userService.findByJobNumber(patient.getJobNumber());
            int wardNumber = user.getWardNumber();
            Message message = new Message((userService.findByWardNumberAndType(wardNumber, 1)).getJobNumber(), 1);

            if (wardNumber == 1) {
                message.setMessage("ID: " + patient.getId() + " Name: " + patient.getName() + " 满足出院条件");
            } else {
                message.setMessage("ID: " + patient.getId() + " Name: " + patient.getName() + " 病情好转可以考虑降级");
            }
            messageService.getMessageRepository().save(message);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "添加成功");
        map.put("result", 1);
        return ResponseEntity.ok(map);
    }
}
