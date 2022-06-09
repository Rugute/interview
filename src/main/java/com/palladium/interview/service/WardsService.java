package com.palladium.interview.service;

import com.palladium.interview.model.SubCounties;
import com.palladium.interview.model.Wards;
import com.palladium.interview.repository.SubCountiesRepository;
import com.palladium.interview.repository.WardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("wardsService")
public class WardsService {
    Date nowDate = new Date();
    private WardsRepository wardsRepository;
    @Autowired
    public WardsService(WardsRepository wardsRepository) {
        this.wardsRepository = wardsRepository;
    }
    public List<Wards> getWradsBySubCountyID(int id){return  wardsRepository.findBySubcountyid(id);}


}

