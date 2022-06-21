package ymlai87416.sd.readapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ymlai87416.sd.dto.Message;
import ymlai87416.sd.dto.User;
import ymlai87416.sd.readapi.repository.MessageRepository;
import ymlai87416.sd.readapi.repository.UserRepository;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Cacheable(value = "userCache", key="#id")
    public User findById(int id){
        return userRepository.findById(id);
    }

    @Cacheable(value = "nameCache", key="#name", unless="#result == null")
    public User findByName(String name){
        return userRepository.findByName(name);
    }
}
