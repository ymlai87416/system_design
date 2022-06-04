package ymlai87416.sd.writeapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ymlai87416.sd.dto.Message;
import ymlai87416.sd.writeapi.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @CacheEvict(value="message", key="#msg.userId")
    public Message save(Message msg){
        return messageRepository.save(msg);
    }
}
