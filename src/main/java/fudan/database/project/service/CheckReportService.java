package fudan.database.project.service;

import fudan.database.project.repository.CheckReportRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class CheckReportService {
    private CheckReportRepository checkReportRepository;

    CheckReportService(CheckReportRepository checkReportRepository) {
        this.checkReportRepository = checkReportRepository;
    }

}
