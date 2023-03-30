package ymlai87416.sd.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ymlai87416.sd.dto.FCMNotification;

import java.util.List;

@FeignClient(name="readApi")
public interface ReadApi {
    @RequestMapping("/fcm/user/{id}")
    List<FCMNotification> getFCMNotificationByUser(@PathVariable int userId);

    @RequestMapping("/fcm/user/{id}/{type}")
    FCMNotification getFCMNotificationByUserAndType(@PathVariable int userId, @PathVariable String type);
}
