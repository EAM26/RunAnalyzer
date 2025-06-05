package com.eamcode.RunAnalyzer.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class DataRow {


    @CsvBindByName(column = "Time")
    private String time;
    @CsvBindByName(column = "HR (bpm)")
    private String heartRate;
    @CsvBindByName(column = "Distances (m)")
    private String distance;

}
