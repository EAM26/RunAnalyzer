package com.eamcode.RunAnalyzer.model;

import com.eamcode.RunAnalyzer.util.LocalTimeConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;

import java.time.LocalTime;

@Data
public class DataRow {


    @CsvCustomBindByName(column = "Time", converter = LocalTimeConverter.class)
    private LocalTime time;
    @CsvBindByName(column = "HR (bpm)")
    private String heartRate;
    @CsvBindByName(column = "Distances (m)")
    private String distance;

}
