package com.palladium.interview.repository;

import com.palladium.interview.model.ETL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface ETLRepository extends JpaRepository<ETL, Long> {
    @Query(value = "CALL sp_get_patient_programs();", nativeQuery = true)
    Map<String, Object> testSp();

}
