package com.eamcode.RunAnalyzer.repository;

import com.eamcode.RunAnalyzer.model.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {
}
