package fudan.database.project.service;

import fudan.database.project.domain.*;
import fudan.database.project.repository.PatientRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Service
public class PatientService {
    private PatientRepository patientRepository;
    private BedService bedService;
    private UserService userService;
    private WNursePatientService wNursePatientService;
    private RecordService recordService;
    private CheckReportService checkReportService;
    private MessageService messageService;

    @Autowired
    public PatientService(PatientRepository patientRepository, BedService bedService, UserService userService, WNursePatientService wNursePatientService, RecordService recordService, CheckReportService checkReportService, MessageService messageService) {
        this.patientRepository = patientRepository;
        this.bedService = bedService;
        this.userService = userService;
        this.wNursePatientService = wNursePatientService;
        this.recordService = recordService;
        this.checkReportService = checkReportService;
        this.messageService = messageService;
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

    public Patient findByBedId(int bedId) {
        return patientRepository.findByBedId(bedId);
    }

    public List<Patient> findAllByStatusAndGrade(int status, int grade) {
        return patientRepository.findAllByStatusAndGrade(status, grade);
    }

    public List<Patient> findWaitingToRefer(int wardNumber) {
        if (wardNumber == 4) {
            return findAllByStatus(4);
        }
        List<Patient> patients = new ArrayList<>();
        List<Bed> wardBeds = bedService.findAllByWardNumber(wardNumber);
        for (Bed bed : wardBeds) {
            if (bed.getStatus() == 1 && findByBedId(bed.getId()).getGrade() != wardNumber) {
                patients.add(findByBedId(bed.getPatientId()));
            }
        }
        return patients;
    }

    public void AutoReferral() {
        for (int i = 4; i > 0; i--) {
            List<Patient> patients = findWaitingToRefer(i);
            for (Patient patient : patients) {
                System.out.println(patient.getId());
                patientRefer(patient);
            }
        }
    }

    public void patientDead(Patient patient) {
        Bed bed = bedService.findById(patient.getBedId());
        bed.setStatus(0);
        bed.setPatientId(-1);
        bedService.getBedRepository().save(bed);
        patient.setBedId(-1);
        patient.setJobNumber(-1);
        patientRepository.save(patient);
        wNursePatientService.deleteByPatientId(patient.getId());
    }

    public void patientRefer(Patient patient) {
        int toWardNumber = patient.getGrade();
        List<User> wNurses = userService.findAllFreeWNurses(toWardNumber);
        List<Bed> beds = bedService.findAllFreeBeds(toWardNumber);
        if (wNurses.size() > 0 && beds.size() > 0) {
            Bed bed = beds.get(0);
            User wNurse = wNurses.get(0);
            if (patient.getBedId() != -1) {
                Bed oldBed = bedService.findById(patient.getBedId());
                oldBed.setStatus(0);
                oldBed.setPatientId(-1);
                bedService.getBedRepository().save(oldBed);
                wNursePatientService.deleteByPatientId(patient.getId());
            }
            bed.setStatus(1);
            bed.setPatientId(patient.getId());
            bedService.getBedRepository().save(bed);
            patient.setJobNumber(wNurse.getJobNumber());
            patient.setBedId(bed.getId());
            patient.setStatus(2);
            patientRepository.save(patient);
            wNursePatientService.getWNursePatientRepository().save(new WNursePatient(wNurse.getJobNumber(), patient.getId()));
            User user = userService.findByWardNumberAndType(toWardNumber, 2);
            Message message = new Message(user.getJobNumber(), "ID: " + patient.getId() + " Name: " + patient.getName() + " 转入当前病区", 1);
            messageService.getMessageRepository().save(message);
            AutoReferral();
        }
    }

    //还要判断是否可以出院
    public boolean canLeaveHospital(int patientId) {
        List<Record> records = recordService.findByPatientId(patientId);
        int size = 0;
        records.sort(new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                if (o1.getDate().getTime() - o2.getDate().getTime() > 0) {
                    return 1;
                } else if (o1.getDate().getTime() == o2.getDate().getTime()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        for (Record record : records) {
            size++;
        }
        if (size < 3) {
            return false;
        }
        long lastTime = records.get(size - 1).getDate().getTime();
        int mark = 0;
        for (int i = size - 2; i >= 0; i--) {
            long startTime = records.get(i).getDate().getTime();
            if (lastTime - startTime > 0 && lastTime - startTime <= 24 * 60 * 60 * 1000 && records.get(i).getTemperature() < 37.3) {
                mark++;
            } else {
                return false;
            }
            if (mark == 3) {
                break;
            }
        }
        List<CheckReport> checkReports = checkReportService.findAllPatientID(patientId);
        checkReports.sort(new Comparator<CheckReport>() {
            @Override
            public int compare(CheckReport o1, CheckReport o2) {
                if (o1.getDate().getTime() - o2.getDate().getTime() > 0) {
                    return 1;
                } else if (o1.getDate().getTime() == o2.getDate().getTime()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        size = 0;
        for (CheckReport checkReport : checkReports) {
            size++;
        }
        System.out.println(size);
        if (size < 2) {
            return false;
        }
        if (checkReports.get(size - 1).getCheckResult() != 1) {
            return false;
        }
        lastTime = checkReports.get(size - 1).getDate().getTime();
        System.out.println(lastTime);
        for (int i = size - 2; i >= 0; i--) {
            int checkResult = checkReports.get(i).getCheckResult();
            long startTime = checkReports.get(i).getDate().getTime();
            System.out.println(lastTime - startTime);
            if (checkResult == 2) {
                return false;
            } else if (lastTime - startTime >= 24 * 60 * 60 * 1000) {
                return true;
            }
        }
        return false;
    }

}
