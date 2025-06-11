package com.eamcode.RunAnalyzer.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class PhaseRequest {
    private LocalTime startTime;
    private LocalTime stopTime;
    private String category;
    private Long reportId;
}
