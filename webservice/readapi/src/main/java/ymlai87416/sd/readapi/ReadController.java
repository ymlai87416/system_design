package ymlai87416.sd.readapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import ymlai87416.sd.dto.*;
import ymlai87416.sd.readapi.service.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class ReadController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private WordCountService wordCountService;

    @Autowired
    private FCMNotificationService fcmNotificationService;


    @RequestMapping("/users/{id}")
    public User getUserInfo(@PathVariable int id){
        return userService.findById(id);
    }

    @RequestMapping("/users/{id}/messages")
    public List<Message> getUserMessage(@PathVariable int id){

        return messageService.findByUserId(id);
    }

    @RequestMapping("/users/{id}/wordcount")
    public List<WordCount> getUserWordCount(@PathVariable int id){

        return wordCountService.findByIdUserId(id);
    }

    @RequestMapping("/users/q/{username}")
    public User getUserInfoByName(@PathVariable String username){
        return userService.findByName(username);
    }

    @RequestMapping("/fcm/user/{id}")
    public List<FCMNotification> getFCMNotificationByUser(@PathVariable int userId){
        return fcmNotificationService.findByUserId(userId);
    }

}
