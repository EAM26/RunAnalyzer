package com.eamcode.RunAnalyzer.dto;

import com.eamcode.RunAnalyzer.model.MetaData;
import com.eamcode.RunAnalyzer.model.Phase;
import lombok.Data;

import java.util.List;

@Data
public class ReportResponse {

    private Long id;
    private String name;
    private String path;
    private List<Phase> phases;
    private MetaData metaData;
    private List<PhaseGroupSummary> summaries;
}
