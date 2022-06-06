package ymlai87416.sd.writeapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ymlai87416.sd.dto.User;
import ymlai87416.sd.writeapi.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @CacheEvict(value="user", key="#newUser.userId")
    public User save(User newUser){
        return userRepository.save(newUser);
    }
}
