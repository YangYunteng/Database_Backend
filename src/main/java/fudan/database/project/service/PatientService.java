package fudan.database.project.service;

import fudan.database.project.domain.Bed;
import fudan.database.project.domain.Patient;
import fudan.database.project.domain.User;
import fudan.database.project.domain.WNursePatient;
import fudan.database.project.repository.PatientRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class PatientService {
    private PatientRepository patientRepository;
    private BedService bedService;
    private UserService userService;
    private WNursePatientService wNursePatientService;

    @Autowired
    public PatientService(PatientRepository patientRepository, BedService bedService, UserService userService, WNursePatientService wNursePatientService) {
        this.patientRepository = patientRepository;
        this.bedService = bedService;
        this.userService = userService;
        this.wNursePatientService = wNursePatientService;
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

    public List<Patient> findAllByBedId(int bedId) {
        return patientRepository.findAllByBedId(bedId);
    }

    public void AutoReferral() {
        boolean mark = true;
        //只有当系统每个区域不在有病人转入停止
        while (mark) {
            mark = false;
            if (ReferToNewWard(1) || ReferToNewWard(2) || ReferToNewWard(3)) {
                mark = true;
            }
        }
    }

    public boolean ReferToNewWard(int wardNumber) {
        List<Bed> beds = bedService.findAllByStatus(0);
        List<User> wNurses = userService.findAllByWardNumberAndType(wardNumber, 3);
        for (User wNurse : wNurses) {
            if (wNursePatientService.patientNumberOfWNurse(wNurse.getJobNumber()) == 3) {
                wNurses.remove(wNurse);
            }
        }

        //无床位，无护士
        if (beds.size() == 0 || wNurses.size() == 0) {
            return false;
        }
        //有床位，有护士,隔离区人员先转入
        List<Patient> patients = findAllByBedId(-1);
        for (Patient patient : patients) {
            Bed bed = beds.get(0);
            User wNurse = wNurses.get(0);
            //安排床位
            patient.setBedId(bed.getId());
            bed.setPatientId(patient.getId());
            bed.setStatus(1);
            bedService.getBedRepository().save(bed);
            beds.remove(bed);
            //安排护士
            patient.setJobNumber(wNurse.getJobNumber());
            WNursePatient wNursePatient = new WNursePatient(wNurse.getJobNumber(), patient.getId());
            patientRepository.save(patient);
            wNursePatientService.getWNursePatientRepository().save(wNursePatient);
            if (wNursePatientService.patientNumberOfWNurse(wNurse.getJobNumber()) == 3) {
                wNurses.remove(wNurse);
            }
            if (wNurses.size() == 0 || beds.size() == 0) {
                return true;
            }
        }
        patients = findAllByGrade(wardNumber);
        for (Patient patient : patients) {
            if (bedService.wardOfBed(patient.getBedId()) != wardNumber) {
                Bed oldBed = bedService.findById(patient.getBedId());
                oldBed.setStatus(0);
                oldBed.setPatientId(-1);
                wNursePatientService.deleteByPatientId(patient.getId());

                Bed bed = beds.remove(0);
                User wNurse = wNurses.remove(0);
                patient.setBedId(bed.getId());
                bed.setPatientId(patient.getId());
                bed.setStatus(1);

                bedService.getBedRepository().save(bed);
                beds.remove(bed);

                patient.setJobNumber(wNurse.getJobNumber());
                WNursePatient wNursePatient = new WNursePatient(wNurse.getJobNumber(), patient.getId());
                patientRepository.save(patient);
                wNursePatientService.getWNursePatientRepository().save(wNursePatient);
                if (wNursePatientService.patientNumberOfWNurse(wNurse.getJobNumber()) == 3) {
                    wNurses.remove(wNurse);
                }
                if (wNurses.size() == 0 || beds.size() == 0) {
                    return true;
                }
            }
        }
//        List<Patient> patientList = patientService.findAllByGrade(wardNumber);
//        for (Patient patient : patientList) {
//            Bed bed=bedService.findBy
//        }
        //可能还可以有病人转入
        return true;
    }


}
