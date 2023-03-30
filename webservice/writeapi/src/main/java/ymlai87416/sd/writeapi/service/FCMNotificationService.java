package ymlai87416.sd.writeapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ymlai87416.sd.dto.FCMNotification;
import ymlai87416.sd.dto.Message;
import ymlai87416.sd.writeapi.repository.FCMNotificationRepository;
import ymlai87416.sd.writeapi.repository.MessageRepository;

@Service
public class FCMNotificationService {
    @Autowired
    FCMNotificationRepository fcmNotificationRepository;

    @CacheEvict(value="fcm_user", key="#msg.userId")
    public FCMNotification save(FCMNotification msg){
        return fcmNotificationRepository.save(msg);
    }
}
