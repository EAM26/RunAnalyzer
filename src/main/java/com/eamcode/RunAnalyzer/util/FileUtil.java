package com.eamcode.RunAnalyzer.util;

import com.eamcode.RunAnalyzer.model.MetaData;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.repository.MetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileUtil {
    
    private  final MetaDataRepository metaDataRepository;

    public  MetaData createReportFromCsv(Report report, String path) throws IOException {
        MetaData data = new MetaData();
        List<String> allLines = Files.readAllLines(Path.of(path));

        String[] metaHeader = allLines.get(0).split(",");
        String[] metaData = allLines.get(1).split(",");


        for (int i = 0; i < metaHeader.length; i++) {
            String key = metaHeader[i].trim();
            String value = metaData[i].trim();

            switch (key) {
                case "Name" -> data.setName(value);
                case "Sport" -> data.setSport(value);
                case "Date" -> data.setDate(value);
                case "Duration" -> data.setDuration(value);
                case "Total distance (km)" -> data.setTotalDistance(value);
                case "Average heart rate (bpm)" -> data.setHeartRateAvg(value);


            }

        }
        return data;
    }
}
