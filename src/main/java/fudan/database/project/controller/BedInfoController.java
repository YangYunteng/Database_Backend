package fudan.database.project.controller;

import fudan.database.project.controller.request.BedInfoRequest;
import fudan.database.project.domain.Bed;
import fudan.database.project.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
public class BedInfoController {
    private BedService bedService;

    @Autowired
    BedInfoController(BedService bedService) {
        this.bedService = bedService;
    }

    @CrossOrigin
    @PostMapping("/bedInfo")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> bedInfo(@RequestBody BedInfoRequest bedInfoRequest) {
        int wardNumber = bedInfoRequest.getWardNumber();
        List<Bed> beds = bedService.findAllByWardNumber(wardNumber);
        HashMap<String, Object> map = new HashMap<>();
        map.put("bedInfo", beds);
        map.put("result", 1);
        return ResponseEntity.ok(map);
    }
}
