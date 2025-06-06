package com.eamcode.RunAnalyzer.service;

import com.eamcode.RunAnalyzer.model.Analyzer;
import com.eamcode.RunAnalyzer.model.DataRow;
import com.eamcode.RunAnalyzer.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyzerService {

    private final FileUtil fileUtil;

    public Analyzer getAnalysis(String path) throws IOException {
        Analyzer analyzer = new Analyzer();
        analyzer.setDataRows(fileUtil.getDataRowsFromCsv(path));
        List<Double> averageSpeeds = getAllSpeeds(analyzer.getDataRows());
        analyzer.setSpeeds(averageSpeeds);
        return analyzer;
    }


    private List<Double> getAllSpeeds(List<DataRow> dataRowList) {
        List<Double> allSpeeds = new ArrayList<>();
        return allSpeeds;
    }
}
