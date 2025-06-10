package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.model.Analyzer;
import com.eamcode.RunAnalyzer.model.DataRow;
import com.eamcode.RunAnalyzer.model.Phase;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.PhaseRepository;
import com.eamcode.RunAnalyzer.repository.ReportRepository;
import com.eamcode.RunAnalyzer.util.CsvUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyzerService {

    private final CsvUtil csvUtil;
    private final ReportRepository reportRepository;
    private final PhaseRepository phaseRepository;

    public void getAnalysis(Report report) throws IOException {
        Analyzer analyzer = new Analyzer();
        analyzer.setDataRows(csvUtil.getDataRowsFromCsv(report.getPath()));
        for(Phase phase: report.getPhases()) {
            double startDistance = getDistanceOfTime(analyzer, phase.getStartTime());
            double endDistance = getDistanceOfTime(analyzer, phase.getStopTime());
            phase.setDistance(endDistance - startDistance);
            phaseRepository.save(phase);
        }
//        getDistanceByPhase(analyzer, report.getPhases());
//        for(Phase phase: report.getPhases()) {
//            System.out.println("in get analysis call to distance");
//            Double distance = getDistanceByPhase(analyzer, );
//            phase.setDistance(distance);
//
//            phaseRepository.save(phase);
//        }
    }

    private double getDistanceOfTime(Analyzer analyzer, LocalTime time) {
        return analyzer.getDataRows()
                .stream()
                .filter(item -> item.getTime().equals(time))
                .map(DataRow::getDistance)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No value found for time: "));
    }


//    private void getDistanceByPhase(Analyzer analyzer, List<Phase> phases) {
//        Phase previous = null;
//        for (Phase phase: phases) {
//            for (DataRow dataRow : analyzer.getDataRows()) {
//                if (dataRow.getTime().equals(phase.getStop()) && previous != null) {
//                    phase.setDistance(Double.parseDouble(dataRow.getDistance()) - previous.getDistance());
//                    phaseRepository.save(phase);
//                }
//                if(dataRow.getTime().equals(phase.getStop())) {
//                    phase.setDistance(Double.parseDouble(dataRow.getDistance()));
//                    phaseRepository.save(phase);
//                }
//            }
//            previous = phase;
//        }
//    }


    private List<Double> getAllSpeeds(List<DataRow> dataRowList) {
        return new ArrayList<>(Arrays.asList(1., 5d, 4.5));
    }
}
