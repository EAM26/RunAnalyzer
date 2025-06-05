package com.eamcode.RunAnalyzer.repository;

import com.eamcode.RunAnalyzer.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
