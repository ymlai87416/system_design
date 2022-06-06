package ymlai87416.sd.writeapi.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ymlai87416.sd.dto.*;

@Repository
public interface UserRepository  extends CrudRepository<User, Integer>{
}
