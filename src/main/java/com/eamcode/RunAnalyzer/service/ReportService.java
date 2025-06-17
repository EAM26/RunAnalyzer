package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.dto.PhaseGroupSummary;
import com.eamcode.RunAnalyzer.dto.PhaseResponse;
import com.eamcode.RunAnalyzer.dto.ReportResponse;
import com.eamcode.RunAnalyzer.dto.ReportSummaryResponse;
import com.eamcode.RunAnalyzer.model.Phase;
import com.eamcode.RunAnalyzer.model.PhaseCategory;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import com.eamcode.RunAnalyzer.util.CsvUtil;
import com.eamcode.RunAnalyzer.util.SpeedConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final PhaseService phaseService;

    public Report createReport(String path) throws IOException {
        Report report = new Report();

//        MetaData
        report.setMetaData(CsvUtil.getMetaDataFromCSV(path));

        report.setName(report.getMetaData().getDate() + " " + report.getMetaData().getStartTime());
        report.setPath(path);




        return reportRepository.save(report);
    }

    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(this::mapToReportResponse)
                .toList();
    }

    public ReportResponse getSingleReport(Long id) throws FileNotFoundException {
        Optional<Report> optional = reportRepository.findById(id);
        if(optional.isEmpty()) {
            throw new FileNotFoundException("Report not found.");
        }
        Report report = optional.get();
        return mapToReportResponse(report);
    }

    public List<ReportSummaryResponse> getSummaryReports() {
        return reportRepository.findAll()
                .stream()
                .map(this::mapToSummaryResponse)
                .toList();
    }


    private ReportResponse mapToReportResponse(Report report) {
        ReportResponse response = new ReportResponse();
        response.setId(report.getId());
        response.setName(report.getName());
        List<PhaseResponse> phaseResponses = phaseService.mapToListResponses(report.getPhases());
        response.setPhaseResponses(phaseResponses);
        response.setPath(report.getPath());
        response.setMetaData(report.getMetaData());
        response.setRegisteredTime(report.getRegisteredTime());

        if(!report.getPhases().isEmpty()) {
            response.setSummaries(calcSummaries(report));
            response.setRemainingDuration(Duration.between(report.getPhases().getLast().getStopTime(), report.getRegisteredTime()));
        }
        return response;
    }


    private ReportSummaryResponse mapToSummaryResponse(Report report) {
        ReportSummaryResponse summaryResponse = new ReportSummaryResponse();
        summaryResponse.setId(report.getId());
        summaryResponse.setName(report.getName());
        summaryResponse.setPath(report.getPath());
        summaryResponse.setDistance(report.getMetaData().getTotalDistance());
        summaryResponse.setDuration(report.getMetaData().getDuration());
        summaryResponse.setId(report.getId());
        summaryResponse.setId(report.getId());

        return summaryResponse;
    }

    private List<PhaseGroupSummary> calcSummaries(Report report) {
        List<PhaseGroupSummary> summaries = new ArrayList<>();
        for(PhaseCategory category: PhaseCategory.values()) {
            PhaseGroupSummary summary = new PhaseGroupSummary();
            List<Double> allHeartRates = new ArrayList<>();
            summary.setCategory(category);
            for(Phase phase: report.getPhases()) {
                if(category.equals(phase.getCategory())) {
                    summary.setTotalDistance(summary.getTotalDistance() + phase.getDistance());
                    summary.setTotalDuration(summary.getTotalDuration().plus(phase.getDuration()));
                    allHeartRates.add(phase.getHeartRateAvg());
                }
            }
            summary.setAverageSpeed(SpeedConverter.speedInKmh(summary.getTotalDistance(),
                    summary.getTotalDuration()));

            summary.setAverageHeartRate(allHeartRates.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0));

            summaries.add(summary);
        }
        return summaries;
    }


}
