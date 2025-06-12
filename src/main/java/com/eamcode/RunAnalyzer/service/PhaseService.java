package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.dto.PhaseRequest;
import com.eamcode.RunAnalyzer.model.PhaseCategory;
import com.eamcode.RunAnalyzer.util.Analyzer;
import com.eamcode.RunAnalyzer.model.Phase;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.PhaseRepository;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import com.eamcode.RunAnalyzer.util.DurationConverter;
import com.eamcode.RunAnalyzer.util.SpeedConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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

    public List<Phase> createMultiPhase(int multiplier, Long reportId, PhaseCategory category1,
                                        PhaseCategory category2, String duration1, String duration2) throws IOException {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NoSuchElementException("No report found."));

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
            PhaseCategory category = (i % 2 == 0) ? category1 : category2;
            phase.setCategory(category);

//            Phase Duration
            String durationAsSTring = (i % 2 == 0) ? duration1 : duration2;
            Duration duration = DurationConverter.convert(durationAsSTring);
            phase.setDuration(duration);
            phase.setStopTime(phase.getStartTime().plus(duration));

//            Phase Distance
            phase.setDistance(analyzer.calcPhaseDistance(analyzer, phase));

//            Phase Speed
            phase.setSpeed(SpeedConverter.speedInKmh(phase.getDistance(), phase.getDuration()));

//            Phase HearRate
            phase.setHeartRateAvg(calcAvgHeartRate(phase.getStartTime(), phase.getStopTime(), analyzer));
            phasesCreated.add(phase);
        }

        totalPhases.addAll(phasesCreated);
        report.setPhases(totalPhases);

        reportRepository.save(report);
        return report.getPhases();
    }

    private Phase mapToPhase(Phase phase, PhaseRequest request, Analyzer analyzer) {
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new NoSuchElementException("Report not found."));
        phase.setReport(report);

//        Convert to Duration
        phase.setDuration(DurationConverter.convert(request.getDuration()));
        setTimes(phase);

        phase.setCategory(request.getCategory());
        phase.setDistance(analyzer.calcPhaseDistance(analyzer, phase));
        phase.setSpeed(SpeedConverter.speedInKmh(phase.getDistance(), phase.getDuration()));
        phase.setHeartRateAvg(calcAvgHeartRate(phase.getStartTime(), phase.getStopTime(), analyzer));
        return phaseRepository.save(phase);
    }

    private void setTimes(Phase phase) {
        if (!phase.getReport().getPhases().isEmpty()) {
            phase.setStartTime(phase.getReport().getPhases().getLast().getStopTime());
        } else {
            phase.setStartTime(LocalTime.parse("00:00:00"));
        }
        phase.setStopTime(phase.getStartTime().plus(phase.getDuration()));
    }

//    private int calcAvgHeartRate2(LocalTime start, LocalTime stop, Analyzer analyzer) {
//        List<Integer> heartRates = new ArrayList<>();
//        Duration step = Duration.ofSeconds(1);
//        for(LocalTime time = start; !time.isAfter(stop); time = time.plus(step)) {
//            analyzer.getDataRows().stream()
//                    .filter((dataRow)-> dataRow.getTime().truncatedTo(ChronoUnit.SECONDS).equals(time))
//                    .forEach(dataRow -> heartRates.add(Integer.parseInt(dataRow.getHeartRate())));
//        }
//
//        if(!heartRates.isEmpty()) {
//            return (int) heartRates.stream()
//                    .mapToInt(Integer::intValue)
//                    .average()
//                    .orElse(0);
//        } else {
//            return 0;
//        }
//    }

    private double calcAvgHeartRate(LocalTime start, LocalTime stop, Analyzer analyzer) {
        return analyzer.getDataRows().stream()
                .filter(dataRow -> {
                    LocalTime t = dataRow.getTime().truncatedTo(ChronoUnit.SECONDS);
                    return (!t.isBefore(start)) && (!t.isAfter(stop));
                })
                .mapToDouble(dataRow -> Double.parseDouble(dataRow.getHeartRate()))
                .average()
                .orElse(0);
    }

}



