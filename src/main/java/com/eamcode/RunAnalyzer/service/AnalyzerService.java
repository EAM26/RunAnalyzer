package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.model.Analyzer;
import com.eamcode.RunAnalyzer.model.DataRow;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import com.eamcode.RunAnalyzer.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyzerService {

    private final FileUtil fileUtil;
    private final ReportRepository reportRepository;

    public Analyzer getAnalysis(Long reportId) throws IOException {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new FileNotFoundException("No report found"));
        Analyzer analyzer = new Analyzer();
        analyzer.setDataRows(fileUtil.getDataRowsFromCsv(report.getPath()));
        List<Double> averageSpeeds = getAllSpeeds(analyzer.getDataRows());
        analyzer.setSpeeds(averageSpeeds);
        return analyzer;
    }


    private List<Double> getAllSpeeds(List<DataRow> dataRowList) {
        List<Double> allSpeeds = new ArrayList<>();
        return allSpeeds;
    }
}
