package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.dto.PhaseRequest;
import com.eamcode.RunAnalyzer.dto.PhaseResponse;
import com.eamcode.RunAnalyzer.model.PhaseCategory;
import com.eamcode.RunAnalyzer.util.Analyzer;
import com.eamcode.RunAnalyzer.model.Phase;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.PhaseRepository;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import com.eamcode.RunAnalyzer.util.DurationConverter;
import com.eamcode.RunAnalyzer.util.SpeedConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.Phased;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
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

    public PhaseResponse createPhase(PhaseRequest request) throws IOException {
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new FileNotFoundException("No report found."));
        Phase phase = new Phase();
        phase.setReport(report);
        Analyzer analyzer = new Analyzer(report.getPath());

//        Map request to phase and add to report
        report.getPhases().add(mapToPhase(phase, request, analyzer));

        report.setRegisteredTime(analyzer.getEndTime());
        reportRepository.save(report);
        return mapToResponse(report.getPhases().getLast());
    }

    public List<PhaseResponse> createMultiPhase(int multiplier, Long reportId, PhaseCategory category1,
                                                PhaseCategory category2, String duration1, String duration2) throws IOException {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new FileNotFoundException("No report found."));

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

            //            Phase Duration
            String durationAsSTring = (i % 2 == 0) ? duration1 : duration2;
            Duration duration = DurationConverter.convert(durationAsSTring);
            phase.setDuration(duration);

            LocalTime stopTime = phase.getStartTime().plus(phase.getDuration());
            if (stopTime.isAfter(analyzer.getEndTime())) {
                throw new IllegalArgumentException("Time is out of bounds. > " + analyzer.getEndTime());
            }
            phase.setStopTime(stopTime);

//            Phase Category
            PhaseCategory category = (i % 2 == 0) ? category1 : category2;
            phase.setCategory(category);

//            Phase Distance
            phase.setDistance(analyzer.calcPhaseDistance(phase));

//            Phase Speed
            phase.setSpeed(SpeedConverter.speedInKmh(phase.getDistance(), phase.getDuration()));

//            Phase HearRate
            phase.setHeartRateAvg(calcAvgHeartRate(phase.getStartTime(), phase.getStopTime(), analyzer));
            phasesCreated.add(phase);
        }

        totalPhases.addAll(phasesCreated);

        report.setPhases(totalPhases);
        report.setRegisteredTime(analyzer.getEndTime());
        reportRepository.save(report);


        return mapToListResponses(report.getPhases());
    }

    private Phase mapToPhase(Phase phase, PhaseRequest request, Analyzer analyzer) {
//        Report report = reportRepository.findById(request.getReportId())
//                .orElseThrow(() -> new NoSuchElementException("Report not found."));
//        phase.setReport(report);

//        Convert to Duration
        phase.setDuration(DurationConverter.convert(request.getDuration()));
        setTimes(analyzer, phase);

        phase.setCategory(request.getCategory());
        phase.setDistance(analyzer.calcPhaseDistance(phase));
        phase.setSpeed(SpeedConverter.speedInKmh(phase.getDistance(), phase.getDuration()));
        phase.setHeartRateAvg(calcAvgHeartRate(phase.getStartTime(), phase.getStopTime(), analyzer));
        return phase;
    }

    public List<PhaseResponse> mapToListResponses(List<Phase> phases) {
        return phases.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PhaseResponse mapToResponse(Phase phase) {
        PhaseResponse response = new PhaseResponse();
        response.setReportId(phase.getReport().getId());
        response.setStartTime(phase.getStartTime());
        response.setStopTime(phase.getStopTime());
        response.setCategory(phase.getCategory());
        response.setDistance(phase.getDistance());
        response.setSpeed(phase.getSpeed());
        response.setDuration(phase.getDuration());
        response.setHeartRateAvg(phase.getHeartRateAvg());

        return response;
    }

    private void setTimes(Analyzer analyzer, Phase phase) {
        if (!phase.getReport().getPhases().isEmpty()) {
            phase.setStartTime(phase.getReport().getPhases().getLast().getStopTime());
        } else {
            phase.setStartTime(LocalTime.parse("00:00:00"));
        }
        LocalTime stopTime = phase.getStartTime().plus(phase.getDuration());
        if (stopTime.isAfter(analyzer.getEndTime())) {
            throw new IllegalArgumentException("Time is out of bounds. > " + analyzer.getEndTime());
        }
        phase.setStopTime(stopTime);


    }


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



