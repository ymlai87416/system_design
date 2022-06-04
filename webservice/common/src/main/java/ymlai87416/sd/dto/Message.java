package ymlai87416.sd.dto;
import lombok.*;

import javax.persistence.*;
import java.io.*;
import java.util.*;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "message")
public class Message implements Serializable{
    @Id
    private int id;
    @Basic
    private String body;
    @Column(name="user_id")
    private int userId;
    @Column(name="created_at")
    private Date createdAt;
    @Column(name="updated_at")
    private Date updatedAt;

    public Message(int id, String body, int userId, Date createdAt, Date updatedAt){
        this.id = id;
        this.body = body;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
