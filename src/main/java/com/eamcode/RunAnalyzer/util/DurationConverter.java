package com.eamcode.RunAnalyzer.util;

import java.time.Duration;
import java.time.LocalTime;

public class DurationConverter {

    public static Duration convert(String durationAsString) {
        return Duration.between(LocalTime.MIN, LocalTime.parse(durationAsString));
    }
}
