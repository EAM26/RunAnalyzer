package com.eamcode.RunAnalyzer.dto;

import com.eamcode.RunAnalyzer.model.MetaData;
import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Data
public class ReportResponse {

    private Long id;
    private String name;
    private String path;
    private LocalTime registeredTime;
    private Duration remainingDuration;
    private List<PhaseResponse> phaseResponses;
    private MetaData metaData;
    private List<PhaseGroupSummary> summaries;
}
