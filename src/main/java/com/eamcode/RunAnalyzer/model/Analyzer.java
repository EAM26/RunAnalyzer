package com.eamcode.RunAnalyzer.model;

import lombok.Data;

import java.util.List;

@Data
public class Analyzer {

    private List<DataRow> dataRows;
    private List<Double> speeds;



}
