package com.eamcode.RunAnalyzer.dto;

import com.eamcode.RunAnalyzer.model.MetaData;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class ReportSummaryResponse {
    private Long id;
    private String name;
    private String path;
    private String distance;
    private String duration;
}
