package ymlai87416.sd.notification.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Primary
    @Bean
    public FirebaseApp getfirebaseApp() throws IOException {
        //File file = ResourceUtils.getFile("classpath:serviceAccount.json");
        //FileInputStream serviceAccount = new FileInputStream(file);
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccount.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public FirebaseAuth getAuth() throws IOException {
        return FirebaseAuth.getInstance(getfirebaseApp());
    }

    @Bean
    public FirebaseAuth getAuth() throws IOException {
        return FirebaseAuth.getInstance(getfirebaseApp());
    }

}
