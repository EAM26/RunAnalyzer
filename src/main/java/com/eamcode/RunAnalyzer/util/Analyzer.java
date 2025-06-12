package com.eamcode.RunAnalyzer.util;

import com.eamcode.RunAnalyzer.model.DataRow;
import com.eamcode.RunAnalyzer.model.Phase;
import lombok.Data;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

@Data

public class Analyzer {

    public Analyzer(String path) throws IOException {
        this.setDataRows(CsvUtil.getDataRowsFromCsv(path));
    }

    private List<DataRow> dataRows;

    public double calcPhaseDistance(Analyzer analyzer, Phase phase) {
        double startDistance = getDistanceOfTime(analyzer, phase.getStartTime());
        double endDistance = getDistanceOfTime(analyzer, phase.getStopTime());
        return endDistance - startDistance;
    }

    private double getDistanceOfTime(Analyzer analyzer, LocalTime time) {
        return analyzer.getDataRows()
                .stream()
                .filter(item -> item.getTime().equals(time))
                .map(DataRow::getDistance)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No value found for time: "));
    }

}
