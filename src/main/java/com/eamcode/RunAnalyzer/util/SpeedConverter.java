package com.eamcode.RunAnalyzer.util;

import java.time.Duration;

public class SpeedConverter {
    public static double calculateSpeedInKmh(double distanceMeters, Duration duration) {
        long seconds = duration.getSeconds();
        if (seconds == 0) return 0;
        return (distanceMeters * 3.6) / seconds;
    }

}
