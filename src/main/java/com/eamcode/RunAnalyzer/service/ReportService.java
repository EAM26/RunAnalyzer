package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.dto.ReportResponse;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import com.eamcode.RunAnalyzer.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final FileUtil fileUtil;

    public Report createReport(String path) throws IOException {
        Report report = new Report();
        report.setMetaData(fileUtil.getMetaDataFromCSV(path));
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
        response.setPhasesInterval(report.getPhasesInterval());
        response.setPath(report.getPath());
        response.setMetaData(report.getMetaData());

        return response;
    }
}
