package com.eamcode.RunAnalyzer.controller;

import com.eamcode.RunAnalyzer.dto.ReportResponse;
import com.eamcode.RunAnalyzer.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> createReport(@RequestParam String path)  {
        try {
            return ResponseEntity.ok(reportService.createReport(path));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File not found.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(reportService.getSingleReport(id));
    }
}
