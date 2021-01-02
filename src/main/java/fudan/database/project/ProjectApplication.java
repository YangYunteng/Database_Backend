package fudan.database.project;

import fudan.database.project.domain.User;
import fudan.database.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataloader(UserRepository userRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                final int WARD_NUM = 3;
                final int DOC_NUM = 1;
                final int HNURSE_NUM = 1;
                final int WNURSE_NUM = 3;
                final int ENURSE_NUM = 1;
                int jobNumber = 111111;
                final String telepone="13853725666";
                for (int i = 0; i < WARD_NUM; i++) {
                    for (int j = 0; j < DOC_NUM; j++) {
                        if (userRepository.findByJobNumber(jobNumber) == null) {
                            userRepository.save(new User(jobNumber, "doctor" + (i + 1), "12345", 1, (i + 1), telepone));
                        }
                        jobNumber++;
                    }
                    for (int j = 0; j < HNURSE_NUM; j++) {
                        if (userRepository.findByJobNumber(jobNumber) == null) {
                            userRepository.save(new User(jobNumber, "hnurse" + (i + 1), "12345", 2, (i + 1), telepone));
                        }
                        jobNumber++;
                    }
                    for (int j = 0; j < WNURSE_NUM; j++) {
                        if (userRepository.findByJobNumber(jobNumber) == null) {
                            userRepository.save(new User(jobNumber, "wnurse" + (i * 3 + j), "12345", 3, (i + 1), telepone));
                        }
                        jobNumber++;
                    }
                }
                for (int i = 0; i < ENURSE_NUM; i++) {
                    if (userRepository.findByJobNumber(jobNumber) == null) {
                        userRepository.save(new User(jobNumber, "enurse1", "12345", 4, (i + 1), telepone));
                    }
                }
            }
        };
    }
}
