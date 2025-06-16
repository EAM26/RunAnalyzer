package com.eamcode.RunAnalyzer.controller;

import com.eamcode.RunAnalyzer.dto.ReportResponse;
import com.eamcode.RunAnalyzer.dto.ReportSummaryResponse;
import com.eamcode.RunAnalyzer.model.Report;
import com.eamcode.RunAnalyzer.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/summary")
    public ResponseEntity<List<ReportSummaryResponse>> getSummaryReports() {
        return ResponseEntity.ok(reportService.getSummaryReports());
    }

    @PostMapping
    public ResponseEntity<Report> createReport(@RequestParam String path) throws IOException {
        return ResponseEntity.ok(reportService.createReport(path));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable Long id) throws FileNotFoundException {
        return ResponseEntity.ok(reportService.getSingleReport(id));
    }
}
