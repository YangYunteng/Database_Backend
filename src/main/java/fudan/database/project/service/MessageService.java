package fudan.database.project.service;

import fudan.database.project.domain.Message;
import fudan.database.project.repository.MessageRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class MessageService {
    private MessageRepository messageRepository;

    MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findAllByJobNumber(int jobNumber) {
        return messageRepository.findAllByJobNumber(jobNumber);
    }
    public Message findById(int messageID) {
        return messageRepository.findById(messageID);
    }
}
