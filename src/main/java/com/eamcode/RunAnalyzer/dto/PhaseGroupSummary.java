package com.eamcode.RunAnalyzer.dto;

import com.eamcode.RunAnalyzer.model.PhaseCategory;
import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;

@Data
public class PhaseGroupSummary {

    private PhaseCategory category;
    private double totalDistance;
    private Duration totalDuration = Duration.ZERO;
    private double averageSpeed;
}
