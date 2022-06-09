package com.palladium.interview.repository;

import com.palladium.interview.model.Branch;
import com.palladium.interview.model.Counties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findAll();
    Branch findById(int qid);
    Branch findByCode(String code);

}

