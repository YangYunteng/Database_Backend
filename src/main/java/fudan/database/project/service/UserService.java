package fudan.database.project.service;

import fudan.database.project.domain.User;
import fudan.database.project.domain.WNursePatient;
import fudan.database.project.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private WNursePatientService wNursePatientService;

    @Autowired
    public UserService(UserRepository userRepository, WNursePatientService wNursePatientService) {
        this.wNursePatientService = wNursePatientService;
        this.userRepository = userRepository;
    }

    public boolean isExist(int job_number) {
        User user = userRepository.findByJobNumber(job_number);
        return user != null;
    }

    public User findByJobNumber(int job_number) {
        return userRepository.findByJobNumber(job_number);
    }

    public void updatePassword(String password, int job_number) {
        User user = userRepository.findByJobNumber(job_number);
        user.setPassword(password);
        userRepository.save(user);
    }

    public List<User> findByType(int type) {
        return userRepository.findByType(type);
    }

    public List<User> findByWardNumber(int wardNumber) {
        return userRepository.findByWardNumber(wardNumber);
    }

    public List<User> findAllByWardNumberAndType(int wardNumber, int type) {
        return userRepository.findAllByWardNumberAndType(wardNumber, type);
    }

    public User findByWardNumberAndType(int wardNumber, int type) {
        return userRepository.findByWardNumberAndType(wardNumber, type);
    }

    public List<User> findAllFreeWNurses(int wardNumber) {
        List<User> tempWNurses = findAllByWardNumberAndType(wardNumber, 3);
        List<User> wNurses = new ArrayList<>();
        for (User user : tempWNurses) {
            if (wNursePatientService.findAllByJobNumber(user.getJobNumber()).size() < 3) {
                wNurses.add(user);
            }
        }
        return wNurses;
    }

    public int deleteByJobNumber(int jobNumber){
        return userRepository.deleteByJobNumber(jobNumber);
    }
}
