package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import com.eamcode.RunAnalyzer.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
}
