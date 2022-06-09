package com.palladium.interview.repository;

import com.palladium.interview.model.Counties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountiesRepository extends JpaRepository<Counties, Long> {
    List<Counties> findAll();
    Counties findById(int qid);

}

