package fudan.database.project.repository;


import fudan.database.project.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByJobNumber(int jobNumber);

    Message findById(int id);
}
