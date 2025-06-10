package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.model.Analyzer;
import com.eamcode.RunAnalyzer.model.DataRow;
import com.eamcode.RunAnalyzer.model.Phase;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.PhaseRepository;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import com.eamcode.RunAnalyzer.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyzerService {

    private final FileUtil fileUtil;
    private final ReportRepository reportRepository;
    private final PhaseRepository phaseRepository;

    public void getAnalysis(Report report) throws IOException {
//        Report report = reportRepository.findById(reportId)
//                .orElseThrow(() -> new FileNotFoundException("No report found"));
        Analyzer analyzer = new Analyzer();
        analyzer.setDataRows(fileUtil.getDataRowsFromCsv(report.getPath()));
        for(Phase phase: report.getPhases()) {
            System.out.println("in get analysis call to distance");
            Double distance = getDistanceByPhase(analyzer, phase);
            phase.setDistance(distance);

            phaseRepository.save(phase);
        }
    }

    private Double getDistanceByPhase(Analyzer analyzer, Phase phase) {
        System.out.println("Get distance by phase method running");
        for (DataRow dataRow : analyzer.getDataRows()) {
            if (dataRow.getTime().equals(phase.getStop())) {
                System.out.println("***********");
                System.out.println("***********");
                return Double.parseDouble(dataRow.getDistance());
            }
        }
        return null;
    }


    private List<Double> getAllSpeeds(List<DataRow> dataRowList) {
        return new ArrayList<>(Arrays.asList(1., 5d, 4.5));
    }
}
