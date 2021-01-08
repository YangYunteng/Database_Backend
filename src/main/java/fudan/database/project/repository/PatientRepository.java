package fudan.database.project.repository;

import fudan.database.project.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findById(int id);

    Patient findByBedId(int bedId);

    List<Patient> findAllByJobNumber(int jobNumber);

    List<Patient> findAllByStatus(int status);

    List<Patient> findAllByGrade(int grade);

    List<Patient> findAllByStatusAndGrade(int status, int grade);
}
