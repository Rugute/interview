package com.palladium.interview.service;

import com.palladium.interview.model.Counties;
import com.palladium.interview.model.SubCounties;
import com.palladium.interview.repository.CountiesRepository;
import com.palladium.interview.repository.SubCountiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("subCountiesService")
public class SubCountiesService {
    Date nowDate = new Date();
    private SubCountiesRepository subCountiesRepository;
    @Autowired
    public SubCountiesService(SubCountiesRepository subCountiesRepository) {
        this.subCountiesRepository = subCountiesRepository;
    }
    public List<SubCounties> getSubCountiesByCountyID(int id){return  subCountiesRepository.findByCountyid(id);}


}

