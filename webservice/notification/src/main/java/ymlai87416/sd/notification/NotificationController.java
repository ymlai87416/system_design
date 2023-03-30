package ymlai87416.sd.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ymlai87416.sd.dto.FCMNotification;
import ymlai87416.sd.dto.FCMNotificationId;

import java.util.Date;

@RestController
@RequestMapping("/not/api/v1")
public class NotificationController {
    @Autowired
    WriteApi writeApi;

    @Autowired
    ReadApi readApi;

    @PostMapping("register/{id}/{type}/{token}")
    public void registerUser(@PathVariable final int id,
                             @PathVariable final String type, @PathVariable final String token){
        FCMNotification record = readApi.getFCMNotificationByUserAndType(id, type);
        if(record == null) {
            record.setFcmToken(token);
            record.setUpdatedAt(new Date());
        }
        else {
            record = new FCMNotification();
            record.setId(new FCMNotificationId(id, type));
            record.setFcmToken(token);
            record.setCreatedAt(new Date());
            record.setUpdatedAt(new Date());
        }

        writeApi.saveFCMNotification(record);
    }
}
