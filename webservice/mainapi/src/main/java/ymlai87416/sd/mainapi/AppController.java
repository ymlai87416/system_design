package ymlai87416.sd.mainapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ymlai87416.sd.dto.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class AppController {

    @Autowired
    ReadApi readApi;

    @Autowired
    WriteApi writeApi;

    @RequestMapping("/users/{id}")
    public User getUserInfo(@PathVariable int id) {
        return readApi.getUserInfo(id);
    }

    @RequestMapping(method = RequestMethod.GET, value="/users/{id}/messages")
    public List<Message> getUserMessage(@PathVariable int id) {
        return readApi.getUserMessage(id);
    }

    @RequestMapping("/users/{id}/wordcount")
    public List<WordCount> getUserWordCount(@PathVariable int id) {
        return readApi.getUserWordCount(id);
    }

    @RequestMapping(method= RequestMethod.POST, value = "/users/{id}/messages", consumes = "text/plain")
    public void uploadMessage(@PathVariable int id, @RequestBody String postPayload) {
        writeApi.uploadMessage(id, postPayload);
    }


    @RequestMapping("/login")
    public User login(Principal principal) {
        String name = principal.getName();
        User user = readApi.getUserInfoByName(name);
        if(user == null){
            //TODO: impl
            User newUser = new User();
            newUser = writeApi.createUser(newUser);
            return newUser;
        }
        else
            return user;
    }

    @RequestMapping("/logout")
    public void logout(Principal principal) {
        //TODO: impl
    }
}
