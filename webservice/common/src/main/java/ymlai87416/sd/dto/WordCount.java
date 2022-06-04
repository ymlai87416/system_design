package ymlai87416.sd.dto;

import lombok.*;

import javax.persistence.*;
import java.io.*;
import java.util.*;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "word_count")
public class WordCount implements Serializable {
    @EmbeddedId
    private WordCountId id; // will be set when persisting
    @Basic
    private int count;
    @Column(name="created_at")
    private Date createdAt;
    @Column(name="updated_at")
    private Date updatedAt;

    public WordCount(WordCountId id, int count, Date createAt, Date updateAt) {
        this.id = id;
        this.count = count;
        this.createdAt = createAt;
        this.updatedAt = updateAt;
    }
}
