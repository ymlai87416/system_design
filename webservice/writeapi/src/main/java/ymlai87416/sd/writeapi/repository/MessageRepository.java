package ymlai87416.sd.writeapi.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ymlai87416.sd.dto.*;
import java.util.*;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
}
