package ymlai87416.sd.sparkmasterrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SparkMasterRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkMasterRestApplication.class, args);
	}

}
