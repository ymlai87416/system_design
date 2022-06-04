package ymlai87416.sd.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.util.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User  implements Serializable{
    @Id
    private int id;
    @Basic
    private String name;
    @Basic
    private String email;
    @Basic
    private String password;
    @Column(name = "created_at")
    private Date createdAt;

    public User(int id, String name, String email, String password, Date createAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password =password;
        this.createdAt = createAt;
    }
}
