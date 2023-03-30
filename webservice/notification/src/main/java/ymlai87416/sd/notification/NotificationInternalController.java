package ymlai87416.sd.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ymlai87416.sd.dto.FCMNotification;
import java.util.*;

@RequestMapping("/internal/not/api/v1")
@RestController
public class NotificationInternalController {

    @Autowired
    private ReadApi readApi;

    @PostMapping("/send-private-message/{id}")
    public void sendPrivateMessage(@PathVariable final int userId,
                                   @RequestBody final String message) {
        List< FCMNotification> tokens = readApi.getFCMNotificationByUser(userId);

        //TODO: call firebase to send message

        return;
    }

}
