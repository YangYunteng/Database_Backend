package fudan.database.project.service;

import fudan.database.project.domain.CheckReport;
import fudan.database.project.repository.CheckReportRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class CheckReportService {
    private CheckReportRepository checkReportRepository;

    CheckReportService(CheckReportRepository checkReportRepository) {
        this.checkReportRepository = checkReportRepository;
    }

    public List<CheckReport> findAllPatientID(int patientID) {
        return checkReportRepository.findAllByPatientId(patientID);
    }
}
