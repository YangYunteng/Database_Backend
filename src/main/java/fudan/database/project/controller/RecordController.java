package fudan.database.project.controller;

import fudan.database.project.controller.request.RecordRequest;
import fudan.database.project.domain.CheckReport;
import fudan.database.project.domain.Patient;
import fudan.database.project.domain.Record;
import fudan.database.project.service.CheckReportService;
import fudan.database.project.service.PatientService;
import fudan.database.project.service.RecordService;
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

    @Autowired
    RecordController(RecordService recordService, CheckReportService checkReportService) {
        this.recordService = recordService;
        this.checkReportService = checkReportService;
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
        Record record = new Record(patientId, temperature, symptoms, status, date);
        recordService.getRecordRepository().save(record);
        checkReportService.getCheckReportRepository().save(new CheckReport(patientId, checkResult, date));
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "添加成功");
        map.put("result", 1);
        return ResponseEntity.ok(map);
    }
}
