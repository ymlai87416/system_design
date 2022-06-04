package ymlai87416.sd.writeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
@EnableCaching
public class WriteapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WriteapiApplication.class, args);
    }

}
