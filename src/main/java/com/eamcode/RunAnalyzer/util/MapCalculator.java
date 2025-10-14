package com.eamcode.RunAnalyzer.util;

import java.util.Map;

public class MapCalculator {

    public static double sumKeys(Map<Long, Long> numbersMap) {
        return numbersMap
                .keySet()
                .stream()
                .mapToDouble(Long::longValue)
                .sum();
    }
}
