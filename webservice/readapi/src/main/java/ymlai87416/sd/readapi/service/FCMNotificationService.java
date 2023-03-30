package ymlai87416.sd.readapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import ymlai87416.sd.dto.*;
import ymlai87416.sd.readapi.repository.*;

import java.util.List;

public class FCMNotificationService {

    @Autowired
    private FCMNotificationRepository fcmNotificationRepository;

    @Cacheable(value="fcm_user", key="#id")
    public List<FCMNotification> findByUserId(int id) {
        return fcmNotificationRepository.findByUserId(id);
    }

}
