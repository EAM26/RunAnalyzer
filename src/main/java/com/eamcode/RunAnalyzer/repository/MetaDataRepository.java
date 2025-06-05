package com.eamcode.RunAnalyzer.repository;

import com.eamcode.RunAnalyzer.model.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaDataRepository extends JpaRepository<MetaData, Long> {
}
