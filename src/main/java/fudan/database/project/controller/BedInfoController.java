package fudan.database.project.controller;

import fudan.database.project.controller.request.BedInfoRequest;
import fudan.database.project.domain.Bed;
import fudan.database.project.service.BedService;
import fudan.database.project.service.PatientService;
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

@Data
@Controller
public class BedInfoController {
    private BedService bedService;
    private PatientService patientService;

    @Autowired
    BedInfoController(BedService bedService, PatientService patientService) {
        this.bedService = bedService;
        this.patientService = patientService;
    }

    @CrossOrigin
    @PostMapping("/bedInfo")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> bedInfo(@RequestBody BedInfoRequest bedInfoRequest) {
        int wardNumber = bedInfoRequest.getWardNumber();
        List<Bed> beds = bedService.findAllByWardNumber(wardNumber);
        List<BedInfo> bedInfos = new ArrayList<>();
        for (Bed bed : beds) {
            BedInfo bedInfo = new BedInfo(bed.getWardNumber(), bed.getRoomNumber(), bed.getBedNumber(), bed.getStatus());
            if (bed.getPatientId() == -1) {
                bedInfo.setPatientName("");
                bedInfo.setPatientID("");
            } else {
                bedInfo.setPatientID(bed.getPatientId() + "");
                bedInfo.setPatientName(patientService.findById(bed.getPatientId()).getName());
            }
            bedInfos.add(bedInfo);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("bedInfo", bedInfos);
        map.put("result", 1);
        return ResponseEntity.ok(map);
    }

    @Data
    class BedInfo {
        private int wardNumber;
        private int roomNumber;
        private int bedNumber;
        private int status;
        private String patientID;
        private String patientName;

        BedInfo() {
        }

        BedInfo(int wardNumber, int roomNumber, int bedNumber, int status, String patientID, String patientName) {
            this.wardNumber = wardNumber;
            this.roomNumber = roomNumber;
            this.bedNumber = bedNumber;
            this.status = status;
            this.patientID = patientID;
            this.patientName = patientName;
        }

        BedInfo(int wardNumber, int roomNumber, int bedNumber, int status) {
            this.wardNumber = wardNumber;
            this.roomNumber = roomNumber;
            this.bedNumber = bedNumber;
            this.status = status;
        }
    }
}
