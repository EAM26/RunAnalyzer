package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.dto.PhaseGroupSummary;
import com.eamcode.RunAnalyzer.dto.ReportResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    
    public Report createReport(String path) throws IOException {
        Report report = new Report();

//        MetaData
        report.setMetaData(CsvUtil.getMetaDataFromCSV(path));
        report.setName(report.getMetaData().getDate());
        report.setPath(path);

        return reportRepository.save(report);
    }

    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();

    }


    private ReportResponse mapToDto(Report report) {
        ReportResponse response = new ReportResponse();
        response.setId(report.getId());
        response.setName(report.getName());
        response.setPhases(report.getPhases());
        response.setPath(report.getPath());
        response.setMetaData(report.getMetaData());

        if(!report.getPhases().isEmpty()) {
            response.setSummaries(calcSummaries(report));
        }

        return response;
    }

    private List<PhaseGroupSummary> calcSummaries(Report report) {
        List<PhaseGroupSummary> summaries = new ArrayList<>();
        for(PhaseCategory category: PhaseCategory.values()) {
            PhaseGroupSummary summary = new PhaseGroupSummary();
            summary.setCategory(category);
            for(Phase phase: report.getPhases()) {
                if(category.equals(phase.getCategory())) {
                    summary.setTotalDistance(summary.getTotalDistance() + phase.getDistance());
                    summary.setTotalDuration(summary.getTotalDuration().plus(phase.getDuration()));
                    summary.setAverageSpeed(SpeedConverter.speedInKmh(summary.getTotalDistance(),
                            summary.getTotalDuration()));
                }
            }
            summaries.add(summary);
        }
        return summaries;
    }

    public ReportResponse getSingleReport(Long id) throws FileNotFoundException {
        Optional<Report> optional = reportRepository.findById(id);
        if(optional.isEmpty()) {
            throw new FileNotFoundException("Report not found.");
        }
        Report report = optional.get();
        return mapToDto(report);
    }
}
