package ymlai87416.sd.readapi.repository;

import org.springframework.data.repository.CrudRepository;
import ymlai87416.sd.dto.*;

import java.util.List;

public interface FCMNotificationRepository  extends CrudRepository<FCMNotification, FCMNotificationId> {

    List<FCMNotification> findByUserId(int userId);

}
