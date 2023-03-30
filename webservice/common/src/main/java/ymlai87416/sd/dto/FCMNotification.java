package ymlai87416.sd.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "fcm_notification")
public class FCMNotification {
    @EmbeddedId
    private FCMNotificationId id;
    @Column(name="fcm_token")
    private String fcmToken;
    @Column(name="created_at")
    private Date createdAt;
    @Column(name="updated_at")
    private Date updatedAt;
}
