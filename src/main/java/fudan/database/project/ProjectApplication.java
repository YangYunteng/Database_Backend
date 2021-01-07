package fudan.database.project;

import fudan.database.project.domain.Bed;
import fudan.database.project.domain.User;
import fudan.database.project.repository.BedRepository;
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
    public CommandLineRunner dataloader(UserRepository userRepository, BedRepository bedRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                final int WARD_NUM = 3;
                final int DOC_NUM = 1;
                final int HNURSE_NUM = 1;
                final int WNURSE_NUM = 3;
                final int ENURSE_NUM = 1;
                int jobNumber = 1;
                final String telepone = "13853725666";
                for (int i = 0; i < WARD_NUM; i++) {
                    for (int j = 0; j < DOC_NUM; j++) {
                        if (userRepository.findByJobNumber(jobNumber) == null) {
                            userRepository.save(new User("doctor" + (i + 1), "12345", 1, (i + 1), telepone));
                        }
                        jobNumber++;
                    }
                    for (int j = 0; j < HNURSE_NUM; j++) {
                        if (userRepository.findByJobNumber(jobNumber) == null) {
                            userRepository.save(new User("hnurse" + (i + 1), "12345", 2, (i + 1), telepone));
                        }
                        jobNumber++;
                    }
                    for (int j = 0; j < WNURSE_NUM; j++) {
                        if (userRepository.findByJobNumber(jobNumber) == null) {
                            User user = new User("wnurse" + (i * 3 + j + 1), "12345", 3, (i + 1), telepone);
                            userRepository.save(user);
                        }
                        jobNumber++;
                    }
                }
                for (int i = 0; i < ENURSE_NUM; i++) {
                    if (userRepository.findByJobNumber(jobNumber) == null) {
                        userRepository.save(new User("enurse1", "12345", 4, (i + 1), telepone));
                    }
                }

                final int WardNumber = 3;
                final int RoomPerWard = 3;
                final int BedPerRoom = 3;

                for (int i = 1; i <= WardNumber; i++) {
                    for (int j = 1; j <= RoomPerWard; j++) {
                        for (int k = 1; k <= BedPerRoom; k++) {
                            Bed bed = new Bed(k, j, i, 0, -1);
                            if (bedRepository.findByBedNumberAndRoomNumberAndWardNumber(k, j, i) == null) {
                                bedRepository.save(bed);
                            }
                        }
                    }
                }
            }
        };
    }
}
