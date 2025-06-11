package com.eamcode.RunAnalyzer.dto;

import lombok.Data;

@Data
public class PhaseRequest {

    private String duration;
    private String category;
    private Long reportId;

}
