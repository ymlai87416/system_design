package ymlai87416.sd.mainapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ymlai87416.sd.dto.*;

import java.util.List;
@FeignClient(name="writeApi")
public interface WriteApi {

    @RequestMapping(method= RequestMethod.POST, value = "/api/v1/users/{id}/messages", consumes = "text/plain")
    void uploadMessage(@PathVariable int id, @RequestBody String postPayload);

    @RequestMapping(method= RequestMethod.POST, value = "/api/v1/users/new", consumes = "text/json")
    User createUser(@RequestBody User newUser);

}
