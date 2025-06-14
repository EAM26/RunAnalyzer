package com.eamcode.RunAnalyzer.dto;

import com.eamcode.RunAnalyzer.model.PhaseCategory;
import com.eamcode.RunAnalyzer.model.Report;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;

@Data
public class PhaseResponse {

    private Long reportId;

    private LocalTime startTime;
    private LocalTime stopTime;

    @Enumerated(EnumType.STRING)
    private PhaseCategory category;

    private Double distance;
    private Double speed;
    private Duration duration;
    private double heartRateAvg;
}
