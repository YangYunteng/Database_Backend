package fudan.database.project.repository;

import fudan.database.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByJobNumber(int job_number);

    //查找不同身份的User
    List<User> findByType(int type);

    List<User> findByWardNumber(int wardNumber);

    List<User> findAllByWardNumberAndType(int wardNumber, int type);

    User findByWardNumberAndType(int wardNumber, int type);

    int deleteByJobNumber(int jobNumber);
}
