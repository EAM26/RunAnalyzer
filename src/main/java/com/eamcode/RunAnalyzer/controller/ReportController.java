package com.eamcode.RunAnalyzer.controller;

import com.eamcode.RunAnalyzer.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<?> createReport(@RequestParam String path)  {
        try {
            return ResponseEntity.ok(reportService.createReport(path));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File not found.");
        }
    }
}
