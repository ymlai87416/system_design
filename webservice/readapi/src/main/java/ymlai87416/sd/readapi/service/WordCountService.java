package ymlai87416.sd.readapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ymlai87416.sd.dto.WordCount;
import ymlai87416.sd.readapi.repository.WordCountRepository;
import java.util.*;

@Service
public class WordCountService {

    @Autowired
    private WordCountRepository wordCountRepository;

    @Cacheable(value="word_count", key="#id")
    public List<WordCount> findByIdUserId(int id){
        return wordCountRepository.findByIdUserId(id);
    }
}
