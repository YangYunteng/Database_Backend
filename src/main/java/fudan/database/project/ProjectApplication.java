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
                final int WNURSE_NUM = 1;
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
                final int RoomPerWard = 1;
                final int BedPersServeRoom = 1;
                final int BedPerMidRoom = 2;
                final int BedPerLightRoom = 4;
                //轻度区
                for (int i = 1; i <= RoomPerWard; i++) {
                    for (int j = 1; j <= BedPerLightRoom; j++) {
                        if (bedRepository.findByBedNumberAndRoomNumberAndWardNumber(j, i, 1) == null) {
                            bedRepository.save(new Bed(j, i, 1, 0, -1));
                        }
                    }
                }
                //中度区
                for (int i = 1; i <= RoomPerWard; i++) {
                    for (int j = 1; j <= BedPerMidRoom; j++) {
                        if (bedRepository.findByBedNumberAndRoomNumberAndWardNumber(j, i, 2) == null) {
                            bedRepository.save(new Bed(j, i, 2, 0, -1));
                        }
                    }
                }

                //重症区一个病房一张床
                for (int i = 1; i <= RoomPerWard; i++) {
                    for (int j = 1; j <= BedPersServeRoom; j++) {
                        if (bedRepository.findByBedNumberAndRoomNumberAndWardNumber(j, i, 3) == null) {
                            bedRepository.save(new Bed(j, i, 3, 0, -1));
                        }
                    }
                }


            }
        };
    }
}
