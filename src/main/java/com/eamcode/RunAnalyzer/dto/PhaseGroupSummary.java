package com.eamcode.RunAnalyzer.dto;

import com.eamcode.RunAnalyzer.model.PhaseCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Duration;

@Data
public class PhaseGroupSummary {

    private PhaseCategory category;
    private double totalDistance;
    private Duration totalDuration = Duration.ZERO;
    private double averageSpeed;
    private double averageHeartRate;

//    @JsonProperty("averageSpeed")
//    public String getFormattedSpeed() {
//        return String.format("%.2f", averageSpeed);
//    }
//
//    @JsonProperty("totalDistance")
//    public String getFormattedTotalDistance() {
//        return String.format("%.2f", totalDistance);
//    }
}
