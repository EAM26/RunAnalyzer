package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.dto.PhaseRequest;
import com.eamcode.RunAnalyzer.util.Analyzer;
import com.eamcode.RunAnalyzer.model.Phase;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.PhaseRepository;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PhaseService {

    private final PhaseRepository phaseRepository;
    private final ReportRepository reportRepository;

    public Phase createPhase(PhaseRequest request) throws IOException {
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new NoSuchElementException("No report found."));
        Analyzer analyzer = new Analyzer(report.getPath());
        Phase phase = new Phase();
        return mapToPhase(phase, request, analyzer);
    }

    public List<Phase> createMultiPhase(int multiplier, Long reportId, String name1,
                                        String name2, String duration1, String duration2) throws IOException {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NoSuchElementException("No report found."));

        double totalDistance = 0d;
        Duration totalDuration = Duration.ZERO;
        List<Phase> totalPhases = report.getPhases();
        List<Phase> phasesCreated = new ArrayList<>();
        Analyzer analyzer = new Analyzer(report.getPath());

        for (int i = 0; i < multiplier * 2; i++) {
            Phase phase = new Phase();
            phase.setReport(report);

//            Phase Times
            if (i == 0) {
                if (!totalPhases.isEmpty()) {
                    phase.setStartTime(totalPhases.getLast().getStopTime());
                } else {
                    phase.setStartTime(LocalTime.MIDNIGHT);
                }
            } else {
                phase.setStartTime(phasesCreated.get(i - 1).getStopTime());
            }

//            Phase Category
            String category = (i % 2 == 0) ? name1 : name2;
            phase.setCategory(category);

//            Phase Duration
            String durationAsSTring = (i % 2 == 0) ? duration1 : duration2;
            Duration duration = Duration.between(LocalTime.MIN, LocalTime.parse(durationAsSTring));
            phase.setStopTime(phase.getStartTime().plus(duration));

//            Phase Distance
            phase.setDistance(analyzer.calcPhaseDistance(analyzer, phase));

//            Phase Speed
            phase.setSpeed(analyzer.calculateSpeedInKmh(phase.getDistance(), duration));

            phasesCreated.add(phase);
        }

        totalPhases.addAll(phasesCreated);
        report.setPhases(totalPhases);

        reportRepository.save(report);
        return report.getPhases();
    }

    private Phase mapToPhase(Phase phase, PhaseRequest request, Analyzer analyzer) throws IOException {
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new NoSuchElementException("Report not found."));
        phase.setReport(report);
        setTimes(phase, request);
        phase.setCategory(request.getCategory());

        phase.setDistance(analyzer.calcPhaseDistance(analyzer, phase));
        Duration duration = Duration.between(LocalTime.MIN, LocalTime.parse(request.getDuration()));
        phase.setSpeed(analyzer.calculateSpeedInKmh(phase.getDistance(), duration));
        return phaseRepository.save(phase);
    }

    private void setTimes(Phase phase, PhaseRequest request) {
        if(!phase.getReport().getPhases().isEmpty()) {
            phase.setStartTime(phase.getReport().getPhases().getLast().getStopTime());
        } else {
            phase.setStartTime(LocalTime.parse("00:00:00"));
        }
        Duration duration = Duration.between(LocalTime.MIN, LocalTime.parse(request.getDuration()));
        phase.setStopTime(phase.getStartTime().plus(duration));
    }
}
