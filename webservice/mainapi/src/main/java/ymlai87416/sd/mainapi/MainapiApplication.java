package ymlai87416.sd.mainapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MainapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainapiApplication.class, args);
    }

}
