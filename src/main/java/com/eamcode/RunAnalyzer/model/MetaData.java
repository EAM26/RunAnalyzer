package com.eamcode.RunAnalyzer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "meta_data")
public class MetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String sport;
    String date;
    String startTime;
    String duration;
    String totalDistance;
    String heartRateAvg;
    String speedAvg;



}
