package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.dto.PhaseRequest;
import com.eamcode.RunAnalyzer.model.Phase;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.PhaseRepository;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import com.eamcode.RunAnalyzer.util.SpeedConverter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.Lint;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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

    public void createMultiPhase(int multiplier, Long reportId, String name1,
                                        String name2, String duration1, String duration2) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NoSuchElementException("No report found."));

        List<Phase> totalPhases = report.getPhases();
        List<Phase> phasesCreated = new ArrayList<>();

        for (int i = 0; i < multiplier * 2; i++) {
            Phase phase = new Phase();
            phase.setReport(report);

            if (i == 0) {
                if (!totalPhases.isEmpty()) {
                    phase.setStartTime(totalPhases.getLast().getStartTime());
                } else {
                    phase.setStartTime(LocalTime.MIDNIGHT);
                }
            } else {
                phase.setStartTime(phasesCreated.get(i - 1).getStopTime());
            }

            String category = (i % 2 == 0) ? name1 : name2;
            phase.setCategory(category);

            String durationAsSTring = (i % 2 == 0) ? duration1 : duration2;
            Duration duration = Duration.between(LocalTime.MIN, LocalTime.parse(durationAsSTring));
            phase.setStopTime(phase.getStartTime().plus(duration));

//            phase.setSpeed(SpeedConverter());

            phasesCreated.add(phase);
        }
        totalPhases.addAll(phasesCreated);
        report.setPhases(totalPhases);

        reportRepository.save(report);
    }

    private Phase mapToPhase(Phase phase, PhaseRequest request) {
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new NoSuchElementException("Report not found."));
        phase.setReport(report);
        phase.setStartTime(request.getStartTime());
        phase.setStopTime(request.getStopTime());
        phase.setCategory(request.getCategory());
        return phaseRepository.save(phase);
    }
}
