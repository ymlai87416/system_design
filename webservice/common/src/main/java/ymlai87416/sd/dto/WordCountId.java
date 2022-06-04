package ymlai87416.sd.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class WordCountId implements Serializable {
    @Column(name="user_id")
    private int userId;
    @Basic
    private String word;

    public WordCountId(int userId, String word){
        this.userId = userId;
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordCountId that = (WordCountId) o;

        if (userId != that.userId) return false;
        return word.equals(that.word);
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + word.hashCode();
        return result;
    }
}
