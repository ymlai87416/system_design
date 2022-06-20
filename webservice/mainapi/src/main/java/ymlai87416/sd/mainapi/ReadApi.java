package ymlai87416.sd.mainapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ymlai87416.sd.dto.*;

import java.util.List;

@FeignClient(name="readApi")
public interface ReadApi {
    @RequestMapping("/api/v1/users/{id}")
    User getUserInfo(@PathVariable int id);

    @RequestMapping("/api/v1/users/{id}/messages")
    List<Message> getUserMessage(@PathVariable int id);

    @RequestMapping("/api/v1/users/{id}/wordcount")
    List<WordCount> getUserWordCount(@PathVariable int id);

    @RequestMapping("/api/v1/users/q/{username}")
    User getUserInfoByName(@PathVariable String username);
}
