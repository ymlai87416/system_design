package ymlai87416.sd.readapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ymlai87416.sd.dto.Message;
import ymlai87416.sd.readapi.repository.MessageRepository;

import java.util.List;
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Cacheable(value="message", key="#id")
    public List<Message> findByUserId(int id) {
        return messageRepository.findByUserId(id);
    }
}
