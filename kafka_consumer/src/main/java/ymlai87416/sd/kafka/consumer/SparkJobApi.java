package ymlai87416.sd.kafka.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="sparkJobApi")
public interface SparkJobApi {

    @RequestMapping(method= RequestMethod.POST, value = "/wordcount", consumes = "text/plain")
    void uploadMessage(@RequestBody String postPayload);
}
