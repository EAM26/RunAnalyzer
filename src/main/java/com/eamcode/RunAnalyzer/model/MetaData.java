package com.eamcode.RunAnalyzer.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
public class MetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String sport;
    String date;
    LocalTime duration;
    String totalDistance;
    String heartRateAvg;



}
