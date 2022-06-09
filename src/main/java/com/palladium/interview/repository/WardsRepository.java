package com.palladium.interview.repository;

import com.palladium.interview.model.SubCounties;
import com.palladium.interview.model.Wards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardsRepository extends JpaRepository<Wards, Long> {
    List<Wards> findAll();
    Wards findById(int qid);
    //List<Wards> findBySubcounty_code(int id);

}