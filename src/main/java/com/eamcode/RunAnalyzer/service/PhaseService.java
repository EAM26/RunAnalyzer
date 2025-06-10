package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.dto.PhaseRequest;
import com.eamcode.RunAnalyzer.model.Phase;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.PhaseRepository;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhaseService {

    private final PhaseRepository phaseRepository;
    private final ReportRepository reportRepository;

    public Phase createPhase(PhaseRequest request) {
        Phase phase = new Phase();
        return mapToPhase(phase, request);
    }

    private Phase mapToPhase(Phase phase, PhaseRequest request)  {
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new NoSuchElementException("Report not found."));
        phase.setReport(report);
        phase.setStartTime(request.getStartTime());
        phase.setStopTime(request.getStopTime());
        phase.setCategory(request.getCategory());
        return phaseRepository.save(phase);
    }
}
