package ymlai87416.sd.mainapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ymlai87416.sd.dto.*;

import java.util.List;

@FeignClient(name="readApi")
public interface ReadApi {
    @RequestMapping("/users/{id}")
    User getUserInfo(@PathVariable int id);

    @RequestMapping("/users/{id}/messages")
    List<Message> getUserMessage(@PathVariable int id);

    @RequestMapping("/users/{id}/wordcount")
    List<WordCount> getUserWordCount(@PathVariable int id);

    @RequestMapping("/users/q/{username}")
    User getUserInfoByName(@PathVariable String username);
}
