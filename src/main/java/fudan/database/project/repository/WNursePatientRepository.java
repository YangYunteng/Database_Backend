package fudan.database.project.repository;

import fudan.database.project.domain.WNursePatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WNursePatientRepository extends JpaRepository<WNursePatient, Integer> {
    List<WNursePatient> findAllByJobNumber(int jobNumber);

    WNursePatient findByPatientId(int patientId);

    int deleteByPatientId(int patientId);
}
