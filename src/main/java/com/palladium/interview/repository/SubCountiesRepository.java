package com.palladium.interview.repository;

import com.palladium.interview.model.Counties;
import com.palladium.interview.model.SubCounties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCountiesRepository extends JpaRepository<SubCounties, Long> {
    List<SubCounties> findAll();
    SubCounties findById(int qid);
   // List<SubCounties> findByCounty_code(int id);

}