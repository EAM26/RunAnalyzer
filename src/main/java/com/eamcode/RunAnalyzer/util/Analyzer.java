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

    public double calcPhaseDistance(Phase phase) {
        double startDistance = getDistanceOfTime(this, phase.getStartTime());
        double endDistance = getDistanceOfTime(this, phase.getStopTime());
        return endDistance - startDistance;
    }

    private double getDistanceOfTime(Analyzer analyzer, LocalTime time) {
//        Start distance always zero
        if(time.equals(LocalTime.of(0,0,0))) {
            return 0d;
        }

//        Correct time by minus 1 second, because the first measurement is at 00:00:00
        LocalTime correctedTime = time.minusSeconds(1);
        return analyzer.getDataRows()
                .stream()
                .filter(item -> item.getTime().equals(correctedTime))
                .map(DataRow::getDistance)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No value found for time: "));
    }

    public LocalTime getEndTime() {
        return this.getDataRows().getLast().getTime();
    }


}
