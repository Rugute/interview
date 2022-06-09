package com.palladium.interview.service;

import com.palladium.interview.model.Branch;
import com.palladium.interview.model.Counties;
import com.palladium.interview.repository.BranchRepository;
import com.palladium.interview.repository.CountiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("branchService")
public class BranchService {
    Date nowDate = new Date();
    private BranchRepository branchRepository;
    @Autowired
    public BranchService(BranchRepository countiesRepository) {
        this.branchRepository = countiesRepository;
    }
    public List<Branch> getAllBraches(){return  branchRepository.findAll();}
    public Branch getBranchByCode(String code){return  branchRepository.findByCode(code);}


}

