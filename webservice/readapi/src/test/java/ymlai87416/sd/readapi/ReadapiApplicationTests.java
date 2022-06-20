package ymlai87416.sd.readapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"ZOOKEEPER_CONN_STR = localhost:22181"})
@TestPropertySource(properties = {"REDIS_HOST = localhost:6379"})
@TestPropertySource(properties = {"DB_URL = jdbc:mysql://localhost:3306/testapp"})
@TestPropertySource(properties = {"DB_USER = root"})
@TestPropertySource(properties = {"DB_PASSWORD = password"})
class ReadapiApplicationTests {

    @Test
    void contextLoads() {
    }

}
