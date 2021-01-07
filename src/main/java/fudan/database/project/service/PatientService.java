package fudan.database.project.service;

import fudan.database.project.domain.Patient;
import fudan.database.project.repository.PatientRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class PatientService {
    private PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient findById(int id) {
        return patientRepository.findById(id);
    }

    public List<Patient> findAllByWNurseJobNumber(int wNurseJobNumber) {
        return patientRepository.findAllByJobNumber(wNurseJobNumber);
    }

    public List<Patient> findAllByStatus(int status) {
        return patientRepository.findAllByStatus(status);
    }

    public List<Patient> findAllByGrade(int grade) {
        return patientRepository.findAllByGrade(grade);
    }
}
