package fudan.database.project.service;

import fudan.database.project.domain.WNursePatient;
import fudan.database.project.repository.WNursePatientRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Data
@Service
@Transactional
public class WNursePatientService {
    private WNursePatientRepository wNursePatientRepository;

    WNursePatientService(WNursePatientRepository wNursePatientRepository) {
        this.wNursePatientRepository = wNursePatientRepository;
    }

    public List<WNursePatient> findAllByJobNumber(int jobNumber) {
        return wNursePatientRepository.findAllByJobNumber(jobNumber);
    }

    public WNursePatient findByPatientId(int patientId) {
        return wNursePatientRepository.findByPatientId(patientId);
    }

    public int patientNumberOfWNurse(int jobNumber) {
        return wNursePatientRepository.findAllByJobNumber(jobNumber).size();
    }

    public int deleteByPatientId(int patientId) {
        return wNursePatientRepository.deleteByPatientId(patientId);
    }
}
