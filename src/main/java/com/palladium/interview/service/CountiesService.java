package com.palladium.interview.service;

import com.palladium.interview.model.Counties;
import com.palladium.interview.repository.CountiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("countiesService")
public class CountiesService {
    Date nowDate = new Date();
    private CountiesRepository countiesRepository;
    @Autowired
    public CountiesService(CountiesRepository countiesRepository) {
        this.countiesRepository = countiesRepository;
    }
    public List<Counties> getAllConties(){return  countiesRepository.findAll();}


}
