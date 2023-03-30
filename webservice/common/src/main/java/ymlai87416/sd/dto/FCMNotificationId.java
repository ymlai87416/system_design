package ymlai87416.sd.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class FCMNotificationId implements Serializable {
    @Column(name="user_id")
    private int userId;
    @Basic
    private String type;

    public FCMNotificationId(int userId, String type){
        this.userId = userId;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FCMNotificationId that = (FCMNotificationId) o;

        if (userId != that.userId) return false;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + type.hashCode();
        return result;
    }
}
