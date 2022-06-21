package ymlai87416.sd.mainapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import ymlai87416.sd.dto.*;
import ymlai87416.sd.mainapi.security.SecurityService;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("http://localhost:3000")
public class AppController {

    @Autowired
    ReadApi readApi;

    @Autowired
    WriteApi writeApi;

    @Autowired
    private SecurityService secuirtyService;

    @RequestMapping("/user")
    public User getCurrentUser() {
        return secuirtyService.getUser();
    }

    @RequestMapping("/user/{id}")
    public User getUserInfo(@PathVariable int id) {
        return readApi.getUserInfo(id);
    }

    @RequestMapping(method = RequestMethod.GET, value="/user/{id}/messages")
    public List<Message> getUserMessage(@PathVariable int id) {
        return readApi.getUserMessage(id);
    }

    @RequestMapping("/user/{id}/wordcount")
    public List<WordCount> getUserWordCount(@PathVariable int id) {
        return readApi.getUserWordCount(id);
    }

    @RequestMapping(method= RequestMethod.POST, value = "/user/{id}/messages", consumes = "text/plain")
    public void uploadMessage(@PathVariable int id, @RequestBody String postPayload) {
        writeApi.uploadMessage(id, postPayload);
    }
}
