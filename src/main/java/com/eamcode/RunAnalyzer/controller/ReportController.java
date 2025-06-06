package com.eamcode.RunAnalyzer.controller;

import com.eamcode.RunAnalyzer.model.Analyzer;
import com.eamcode.RunAnalyzer.service.AnalyzerService;
import com.eamcode.RunAnalyzer.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final AnalyzerService analyzerService;

    @PostMapping
    public ResponseEntity<?> createReport(@RequestParam String path)  {
        try {
            return ResponseEntity.ok(reportService.createReport(path));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File not found.");
        }
    }

//    @GetMapping
//    public ResponseEntity<StringBuilder> showData(@RequestParam String path) throws IOException {
//        StringBuilder sb = reportService.showData(path);
//        return ResponseEntity.ok(sb);
//    }


    @GetMapping("/analyzer")
    public ResponseEntity<Analyzer> analyze(@RequestParam String path) throws IOException {
        Analyzer analyzer = analyzerService.getAnalysis(path);
        return ResponseEntity.ok(analyzer);
    }
}
