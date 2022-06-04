package ymlai87416.sd.mainapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ymlai87416.sd.dto.*;
import java.util.*;

@RestController
public class AppController {

    @Autowired
    ReadApi readApi;

    @Autowired
    WriteApi writeApi;

    @RequestMapping("/users/{id}")
    public User getUserInfo(@PathVariable int id) {
        return readApi.getUserInfo(id);
    }

    @RequestMapping("/users/{id}/messages")
    public List<Message> getUserMessage(@PathVariable int id) {
        return readApi.getUserMessage(id);
    }

    @RequestMapping("/users/{id}/wordcount")
    public List<WordCount> getUserWordCount(@PathVariable int id) {
        return readApi.getUserWordCount(id);
    }

    @RequestMapping(method= RequestMethod.POST, value = "/users/{id}/wordcount", consumes = "text/plain")
    public void uploadMessage(@PathVariable int id, @RequestBody String postPayload) {
        writeApi.uploadMessage(id, postPayload);
    }


}
