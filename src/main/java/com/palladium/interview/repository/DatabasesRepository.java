package com.palladium.interview.repository;

import com.palladium.interview.model.DatabaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatabasesRepository extends JpaRepository<DatabaseInfo, Long> {
    List<DatabaseInfo> findAll();
    DatabaseInfo findByDbname(String dbname);

}
