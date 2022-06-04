package ymlai87416.sd.readapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import ymlai87416.sd.dto.*;
import ymlai87416.sd.readapi.service.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class ReadController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private WordCountService wordCountService;


    @RequestMapping("/users/{id}")
    public User getUserInfo(@PathVariable int id){
        return userService.findById(id);
    }

    @RequestMapping("/users/{id}/messages")
    public List<Message> getUserMessage(@PathVariable int id){
        /*Message m1 = new Message(id, "Preserved defective offending he daughters on or. Rejoiced prospect yet material servants out answered men admitted. Sportsmen certainty prevailed suspected am as. Add stairs admire all answer the nearer yet length. Advantages prosperous remarkably my inhabiting so reasonably be if. Too any appearance announcing impossible one. Out mrs means heart ham tears shall power every.", id, new Date(), new Date());
        Message m2 = new Message(id, "As absolute is by amounted repeated entirely ye returned. These ready timed enjoy might sir yet one since. Years drift never if could forty being no. On estimable dependent as suffering on my. Rank it long have sure in room what as he. Possession travelling sufficient yet our. Talked vanity looked in to. Gay perceive led believed endeavor. Rapturous no of estimable oh therefore direction up. Sons the ever not fine like eyes all sure.", id, new Date(), new Date());
        Message m3 = new Message(id, "Sense child do state to defer mr of forty. Become latter but nor abroad wisdom waited. Was delivered gentleman acuteness but daughters. In as of whole as match asked. Pleasure exertion put add entrance distance drawings. In equally matters showing greatly it as. Want name any wise are able park when. Saw vicinity judgment remember finished men throwing.", id, new Date(), new Date());

        return List.of(m1, m2, m3); */
        return messageService.findByUserId(id);
    }

    @RequestMapping("/users/{id}/wordcount")
    public List<WordCount> getUserWordCount(@PathVariable int id){
        /*
        WordCount wc1 = new WordCount(id, "cloud", 10, new Date(), new Date());
        WordCount wc2 = new WordCount(id, "apple", 12, new Date(), new Date());
        WordCount wc3 = new WordCount(id, "tree", 15, new Date(), new Date());
        WordCount wc4 = new WordCount(id, "cat", 20, new Date(), new Date());

        return List.of(wc1, wc2, wc3, wc4);*/
        return wordCountService.findByIdUserId(id);
    }
}
