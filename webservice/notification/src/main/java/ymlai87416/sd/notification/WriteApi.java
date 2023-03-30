package ymlai87416.sd.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ymlai87416.sd.dto.FCMNotification;
@FeignClient(name="writeApi")
public interface WriteApi {

    @RequestMapping(method= RequestMethod.POST, value = "/api/v1/fcm/token/", consumes = "application/json")
    FCMNotification saveFCMNotification(@RequestBody FCMNotification newToken);
}
