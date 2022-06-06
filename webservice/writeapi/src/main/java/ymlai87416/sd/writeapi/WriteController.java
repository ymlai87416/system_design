package ymlai87416.sd.writeapi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ymlai87416.sd.dto.*;
import ymlai87416.sd.writeapi.service.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class WriteController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    private final WordCountProducer topicProducer;

    @RequestMapping(method= RequestMethod.POST, value = "/users/{id}/messages", consumes = "text/plain")
    public void uploadMessage(@PathVariable int id, @RequestBody String postPayload){
        Message msg = new Message(0, postPayload, id, new Date(), new Date());
        messageService.save(msg);

        //now put the message in kafka queue.
        topicProducer.send(id+ ":" + postPayload);
    }


    @RequestMapping(method= RequestMethod.POST, value = "/testkafka/{id}", consumes = "text/plain")
    public void testKafka(@PathVariable int id, @RequestBody String postPayload){
        topicProducer.send(id+ ":" + postPayload);
    }


    @RequestMapping(method= RequestMethod.POST, value = "/users/new", consumes = "text/json")
    public User createUser(@RequestBody User newUser){
        return userService.save(newUser);
    }
}
