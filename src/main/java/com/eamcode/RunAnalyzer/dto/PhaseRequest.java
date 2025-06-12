package com.eamcode.RunAnalyzer.dto;

import com.eamcode.RunAnalyzer.model.PhaseCategory;
import lombok.Data;

@Data
public class PhaseRequest {

    private String duration;
    private PhaseCategory category;
    private Long reportId;

}
