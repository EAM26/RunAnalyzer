package com.eamcode.RunAnalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;

@Data
@Entity
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id")
    @JsonIgnore
    private Report report;

    private LocalTime startTime;
    private LocalTime stopTime;
    private String category;
    private Double distance;
    private Double speed;
    private Duration duration;

    @JsonProperty("distance")
    public String getFormattedDistance() {
        return String.format("%.2f", distance);
    }

    @JsonProperty("speed")
    public String getFormattedSpeed() {
        return String.format("%.2f", speed);
    }

}
