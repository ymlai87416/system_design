package ymlai87416.sd.kafkaconsumer2;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="notificationApi")
public interface NotificationApi {
    @RequestMapping(method= RequestMethod.POST, value = "/notify/{id}", consumes = "text/plain")
    void notifyUser(@PathVariable int id, @RequestBody String postPayload);
}
