package com.eamcode.RunAnalyzer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String sport;
    String date;
    String duration;
    String totalDistance;
    String heartRateAvg;



}
