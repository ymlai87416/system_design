package ymlai87416.sd.readapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ymlai87416.sd.dto.*;
import java.util.*;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findById(int id);

    User findByName(String name);
}
