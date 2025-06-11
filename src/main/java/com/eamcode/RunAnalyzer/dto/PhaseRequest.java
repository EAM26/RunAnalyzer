package com.eamcode.RunAnalyzer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;

@Data
public class PhaseRequest {

    private String duration;
    private String category;
    private Long reportId;

    public Duration getParsedDuration() {
        return Duration.between(LocalTime.MIN, LocalTime.parse(duration));
    }

}
