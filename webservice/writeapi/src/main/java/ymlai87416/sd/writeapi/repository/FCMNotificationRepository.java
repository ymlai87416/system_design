package ymlai87416.sd.writeapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ymlai87416.sd.dto.FCMNotification;
import ymlai87416.sd.dto.FCMNotificationId;
import ymlai87416.sd.dto.Message;

@Repository
public interface FCMNotificationRepository  extends CrudRepository<FCMNotification, FCMNotificationId> {
}
