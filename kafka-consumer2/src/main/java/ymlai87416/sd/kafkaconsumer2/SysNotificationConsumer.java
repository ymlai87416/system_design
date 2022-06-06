package ymlai87416.sd.kafkaconsumer2;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SysNotificationConsumer {
    Logger logger = LoggerFactory.getLogger(SysNotificationConsumer.class);

    @Value("${topic.name.consumer")
    private String topicName;

    @Autowired
    NotificationApi notificationApi;

    @KafkaListener(topics = "${topic.name.consumer}", groupId = "group_id")
    public void consume(ConsumerRecord<String, String> payload){
        logger.info("Get the message: " + payload.value());
        notificationApi.notifyUser(1, "Hello");
    }
}
