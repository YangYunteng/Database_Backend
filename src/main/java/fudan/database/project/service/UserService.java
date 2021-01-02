package fudan.database.project.service;

import fudan.database.project.domain.User;
import fudan.database.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public User getByJob_Number(int job_number) {
        return userRepository.findByJobNumber(job_number);
    }
}
