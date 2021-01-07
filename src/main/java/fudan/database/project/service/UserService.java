package fudan.database.project.service;

import fudan.database.project.domain.User;
import fudan.database.project.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
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

    public User findByWardNumberAndType(int wardNumber,int type){
        return userRepository.findByWardNumberAndType(wardNumber,type);
    }
}
