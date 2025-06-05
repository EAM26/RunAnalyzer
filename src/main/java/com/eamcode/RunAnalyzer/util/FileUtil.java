package com.eamcode.RunAnalyzer.util;

import com.eamcode.RunAnalyzer.model.DataRow;
import com.eamcode.RunAnalyzer.model.MetaData;
import com.eamcode.RunAnalyzer.model.Report;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileUtil {


    public MetaData getMetaDataFromCSV(String path) throws IOException {
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

    public List<DataRow> getDataRowsFromCsv(String path) throws IOException {
        List<String> allLines = Files.readAllLines(Path.of(path));
        List<String> dataRowLines = allLines.subList(2, allLines.size());

        StringBuilder csvString = new StringBuilder();
        for (String line : dataRowLines) {
            csvString.append(line).append("\n");
        }

        try (StringReader reader = new StringReader(csvString.toString())) {

            CsvToBean<DataRow> csvToBean = new CsvToBeanBuilder<DataRow>(reader)
                    .withType(DataRow.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }


}
