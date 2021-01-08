package fudan.database.project.controller;

import fudan.database.project.controller.request.DeleteMessageRequest;
import fudan.database.project.controller.request.MessageRequest;
import fudan.database.project.domain.Message;
import fudan.database.project.service.MessageService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Data
@Controller
public class MessageController {
    private MessageService messageService;

    @Autowired
    MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @CrossOrigin
    @PostMapping("/deleteMessage")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> deleteMessage(@RequestBody DeleteMessageRequest deleteMessageRequest) {
        int messageID = deleteMessageRequest.getMessageID();
        Message message = messageService.findById(messageID);
        message.setStatus(1);
        messageService.getMessageRepository().save(message);
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "已读取");
        map.put("result", 1);
        return ResponseEntity.ok(map);
    }


    @CrossOrigin
    @PostMapping("/message")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> queryMessage(@RequestBody MessageRequest messageRequest) {
        int jobNumber = messageRequest.getJobNumber();
        List<Message> messages = messageService.findAllByJobNumber(jobNumber);
        HashMap<String, Object> map = new HashMap<>();
        map.put("messageData", messages);
        map.put("result", 1);
        return ResponseEntity.ok(map);
    }
}
